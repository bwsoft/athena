# Main CMakeLists.txt to define project level setup
#
# To create CodeBlocks project file use
#    cmake <path to CMakeLists.txt> -G"CodeBlocks - Unix Makefiles"

cmake_minimum_required(VERSION 3.2 FATAL_ERROR)

# Enable the search of cmake modules in extra directories
list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmakemodules")

# include do_test module
include( DoTest )

# Use C++ 11. This needs to be defined ahead of all targets.
# CMAKE_CXX_STANDARD defines the initial value for CXX_STANDARD.
# Define the compiler flags
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")

# Define pre-processor flags
# add_definitions( -Dsomething )

# define project and its version
project("c++-sample")
set(SAMPLE_VERSION_MAJOR 1)
set(SAMPLE_VERSION_MINOR 0)

# Add system introspection
if( UNIX )
  set( OS "unix")
elseif( WIN32 )
  set( OS "win" )
endif()

# check include file for a function
include(CheckIncludeFiles)
check_include_files(malloc.h HAVE_MALLOC_H)
check_include_files("not;exist" HAVE_SYS_MOUNT_H)

# Configure a header file to pass some of the CMake settings to the source code
configure_file("${PROJECT_SOURCE_DIR}/ProjectConfig.h.in" 
   "${PROJECT_BINARY_DIR}/ProjectConfig.h"
)

# add the binary tree to the search path for the include files
include_directories("${PROJECT_BINARY_DIR}")

#include sub projects
add_subdirectory("cmake-sample")
add_subdirectory("lib-sample")

# enable test
include(CTest)

# set installation prefix to be the parent of the top level source directory.
set(CMAKE_INSTALL_PREFIX "${CMAKE_SOURCE_DIR}/..")

# installation
install(FILES "${PROJECT_BINARY_DIR}/ProjectConfig.h" DESTINATION include)
