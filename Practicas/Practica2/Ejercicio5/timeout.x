#define VERSION_NUMBER 1

program TO_PROG {
   version TO_VERSION {
     int some_function() = 1;
     int some_function_to_22() = 2;
     int some_function_to_26() = 3;
   } = VERSION_NUMBER;
} = 555553511;
