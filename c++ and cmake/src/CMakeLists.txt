# Main CMakeLists.txt to define project level setup
#
# To create CodeBlocks project file use
#    cmake <path to CMakeLists.txt> -G"CodeBlocks - Unix Makefiles"

cmake_minimum_required(VERSION 3.2 FATAL_ERROR)

# define project and its version
project("c++-sample")
set(SAMPLE_VERSION_MAJOR 1)
set(SAMPLE_VERSION_MINOR 0)

# Configure a header file to pass some of the CMake settings to the source code
configure_file("${PROJECT_SOURCE_DIR}/ProjectConfig.h.in" 
   "${PROJECT_BINARY_DIR}/ProjectConfig.h"
)

# add the binary tree to the search path for the include files
include_directories("${PROJECT_BINARY_DIR}")

#include sub projects
add_subdirectory("cmake-sample")
add_subdirectory("lib-sample")

# set installation prefix to be the parent of the top level source directory.
set(CMAKE_INSTALL_PREFIX "${CMAKE_SOURCE_DIR}/..")

# enable test
include(CTest)

# installation
install(FILES "${PROJECT_BINARY_DIR}/ProjectConfig.h" DESTINATION include)
