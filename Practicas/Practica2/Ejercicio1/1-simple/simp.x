#define VERSION_NUMBER 1

#define foo 127

struct operands {
	int x;
	int y;
};

program SIMP_PROG {
   version SIMP_VERSION {
     int ADD(operands) = 1;
     int SUB(operands) = 2;
   } = VERSION_NUMBER;
} = 555555555;
