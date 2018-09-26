#define VERSION_NUMBER 1

typedef string stream<>;
typedef string filename<255>;

struct read_request {
	filename name;
	int offset;
  int size;
};

struct read_response {
	int size;
	stream buffer;
};

struct write_request {
  filename name;
	int size;
  stream buffer;
};

program FS_PROG {
   version FS_VERSION {
     read_response pdytr_read(read_request req) = 1;
     int pdytr_write(write_request req) = 2;
     int pdytr_file_size(filename name) = 3;
   } = VERSION_NUMBER;
} = 555553555;
