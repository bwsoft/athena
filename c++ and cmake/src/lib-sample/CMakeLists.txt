add_library(MathFunctions mysqrt.cxx)

file(GLOB headers "*.h")

# Installation
install(TARGETS MathFunctions DESTINATION lib)
install(FILES "${headers}" DESTINATION "include/lib-sample")
