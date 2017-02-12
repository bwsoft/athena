macro( do_test target argv result )
  add_test(${target}${argv} ${target} ${argv})
  set_tests_properties(${target}${argv} PROPERTIES PASS_REGULAR_EXPRESSION ${result})
endmacro( do_test )
