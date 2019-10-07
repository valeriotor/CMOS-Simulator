A small program I wrote to describe the characteristics of a CMOS Transistor. 

You can download the Jar file [here](https://drive.google.com/open?id=1-bAl6nKNmgeiyxbDPp_TWZ0sD4nudPDM). 
You can run it by typing `java -jar CMOS-Simulator.jar` in the command line while in the file's directory.

The project is currently very barebones. At the moment it draws two graphs. The right one represents the currents of both NMOS and PMOS in function of output and input voltages. The left one is the Voltage-Transfer-Characteristic, and puts the output voltage in function of the input one.

This is a very, very basic representation of the CMOS. It does not take into account various physical effects that distinguish a real transistor from its ideal counterpart (e.g. saturation velocity, channel modulation, weak-inversion). It should not be used professionally. It might however help students visualize the way in which modifying the MOSFETs' parameters also modifies the functions.

Yes, I realize the limitations mentioned above are fairly important. Yes, I realize the project is too small in size to be of much use. And yes, I realize this would've been better written in Python than Java. Stop yelling at me.
