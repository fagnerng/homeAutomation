typedef struct User{
	String username;
	String password;
};

typedef struct Device{
	int id;
	String name;
	bool onTime;
	int time;
	int switch_pin;
	int button_pin;
	int last_status;
};
