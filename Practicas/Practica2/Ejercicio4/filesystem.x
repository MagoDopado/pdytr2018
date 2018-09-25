#define VERSION_NUMBER 1

typedef string stream<>;
typedef string filename<255>;

struct read_request {
	filename name;
	int offset;
  int ammount;
};

struct read_response {
	int ammount;
	stream buffer;
};

struct write_request {
  filename name;
	int offset;
  stream buffer;
};

program FS_PROG {
   version FS_VERSION {
     read_response read(read_request req) = 1;
     int write(write_request req) = 2;
   } = VERSION_NUMBER;
} = 555553555;
