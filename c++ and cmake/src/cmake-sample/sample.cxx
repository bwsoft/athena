#include <iostream>
#include <cstdlib>

#include "ProjectConfig.h"
#include "lib-sample/mysqrt.h"

using std::cout;
using std::endl;

int main(int argc, char * argv[]) 
{
   if( argc < 2 )
   {
      cout << argv[0] << " Version " << SAMPLE_VERSION_MAJOR << '.' << SAMPLE_VERSION_MINOR << endl;
      cout << "Platform: " << OS << endl;
      cout << "Usage: " << argv[0] << " number" << endl;
      return 1;
   }

   double input = atof(argv[1]);
   cout << "squart of " << input << " is " << mysqrt(input) << endl;
}
