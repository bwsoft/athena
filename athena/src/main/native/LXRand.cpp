#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/ioctl.h>
#include <linux/random.h>

/**
 * It uses the white noise of the Linux device to generate a random number. This routine will also
 * check if the /dev/random is a true random.
 *
 * If the entropy is not sufficient, no random is generated. It guarantees the precision of randomness.
 *
 * This is a C-style function. To be invoked by C++, add extern "C" in front of the function.
 */
#ifdef __cplusplus
extern "C"
#endif
int getLXRand(int *randInteger) {
	int randomData = open("/dev/random", O_RDONLY);
	int entropy;
	int result = ioctl(randomData, RNDGETENTCNT, &entropy);

	if (!result) {
		// Error - /dev/random isn't actually a random device
		close(randomData);
		printf("Error: /dev/random is not a random device.");
		return -1;
	}

	if (entropy < sizeof(int) * 8) {
		// Error - there's not enough bits of entropy in the random device to fill the buffer
		close(randomData);
		printf("Error: not enough bits of entropy.");
		return -2;
	}

	size_t randomDataLen = 0;
	while (randomDataLen < sizeof(int))
	{
		ssize_t result = read(randomData, ((char*)randInteger) + randomDataLen, (sizeof(int)) - randomDataLen);
		if (result < 0)
		{
			close(randomData);
			printf("Error: failed in reading random bits.");
			return -3;
		}
		randomDataLen += result;
	}
	close(randomData);
	return 0;
}
