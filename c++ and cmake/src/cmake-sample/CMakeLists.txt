# The following macros, do_test, has been defined in the module
# macro( do_test argv result )
#   add_test(CMakeSample${argv} CMakeSample ${argv})
#   set_tests_properties(CMakeSample${argv} PROPERTIES PASS_REGULAR_EXPRESSION ${result})
# endmacro( do_test )

include_directories("${PROJECT_SOURCE_DIR}")

add_executable(CMakeSample sample.cxx)
target_link_libraries (CMakeSample MathFunctions)

# installation
install(TARGETS CMakeSample DESTINATION bin)

# do a bunch of test
include(CTest)

do_test(CMakeSample 25 "25 is 5")
do_test(CMakeSample 26 "26 is 6")
