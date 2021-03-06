#include <stdio.h>
#include <stddef.h>
#include <pwd.h>		/* needed for pw lookup functions */
#include <sys/types.h>          /* needed for pw database types */

typedef char* username;

int * 
byname_1(username *argp)
{
	static int  result;
	struct passwd *pw;

	/* argp is a pointer to: a string (a char *)! */
	pw = getpwnam( *argp );
	if (pw==NULL) {
		/* ERROR ! NO USER FOUND */
	  result = -1;
	} else {
		result = pw->pw_uid;
	}
	return(&result);
}

char *error="ERROR";
username * 
bynum_1(int *argp)
{
	
	static username  result;
	struct passwd *pw;
	
	/* username is a pointer to: a string (a char *)! */
	getpwuid( *argp );
	if (pw==NULL) {
	  /* ERROR ! NO USER FOUND */
	  /* WE CANNOT RETURN A NULL (will screw up xdr filter) */
	  /* Point to an error string */
	  result=error;
	} else {
	  result = pw->pw_name;
	}

	return(&result);
}

int main( int argc, char* argv[] ) {
  char *name;
  int uid;

  if(argc < 2) {
    printf("usage: %s name_or_uid\n", argv[0]);
    exit(1);
  }
  name = argv[1];
  if ((name[0]>='0')&&(name[0]<='9')) {
    uid = atoi(name);
    printf("UID %d, Name is %s\n",uid,bynum_1(&uid));
  } else {
    printf("Name %s, UID is %d\n",name,byname_1(&name));
  }
  return(0);
}