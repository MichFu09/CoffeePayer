Michael Fuller
10/30/2025

Cloud Engineer Code Challenge README

-----------------------------------
-----  How to Build and Run   -----
-----------------------------------

This project is coded in basic Java using VS Code as the IDE. The JDK used was OpenJDK 17.0.16.

To build in VS Code, simply clone the repo or download the "CoffeePayer" directory locally, ensure VS Code is installed with necessary
	Java extensions, open the repo/folder in VS code and run.

If you do not wish to use VS Code. Download the files from the "src" directory and place somewhere locally.
	Ensure that a recent java jdk is accessible from PATH. Navigate to the location you copied the files
	in cmd, PowerShell, or equivalent. Run "javac .\CoffeePayer.java" and then "java .\CoffeePayer.java"
	to run.

-----------------------------------
-----       App Startup       -----
-----------------------------------

Upon initial startup, this application will contain no information about the users or their orders. This is	by design,
	but can be overwritten by seeding the save file that would be generated automatically (see next section for more detail).

Regardless, the program will launch as a CLI with the following:

#########################################################
##              Welcome To Coffee Payer!               ##
#########################################################
##                  Available Actions                  ##
#########################################################
## 1: Add a new member to the list                     ##
## 2: Select today's coffee buyer!                     ##
## 3: Update a member's order                          ##
## 4: Update a member's account balance                ##
## 5: Remove a member from the list     :(             ##
## 6: Print list of members getting coffee             ##
## 7: Print a selected member's order and balance      ##
## 8: Re-print these options                           ##
## 0: Exit application                                 ##
#########################################################

The options are selected by entering the "int" values associated with the selections. The app will then prompt
	the user for any necessary additional input. In this case and others, a minimal amount of input validation
	is provided to ensure that the program doesn't crash, but in keeping with the proposed context of the program,
	this checking is currently rather minimal and will just return the user to the primary action selection context.

-----------------------------------
----- How to Seed The Program -----
-----------------------------------

The project can be run without initial data as mentioned above.

However, if you want to jumpstart with pre-existing data, the expected save file is called "CoffeeList.txt"
	and is expected under the same "src" directory as the rest of the project (NOTE: it will look in the same
	directory, so long as you are running in VS Code which has that set as a source path for the project,
	otherwise, it will look for the file in a "src" subdirectory living alongside the java files.

Entries in this file are expected a 3 comma-separated values of the following form:

[String - name],[String (double format) - price of coffee],[String (double format) - current amount owed]

A 'starting' example of a list would be as follows:

Bob,7.35,0.0
Stephen,3.68,0.0
Bill,6.24,0.0
Katy,6.0,0.0
Anne,5.39,0.0
Peter,8.45,0.0
Jim,2.55,0.0

An 'in-progress' version would be something like:

Bob,7.35,10.14
Stephen,3.68,-17.579999999999995
Bill,6.24,3.4800000000000004
Katy,6.0,2.039999999999999
Anne,5.39,-7.319999999999998
Peter,8.45,-14.309999999999999
Jim,2.55,15.3

-----------------------------------
-----   Program Assumptions   -----
-----------------------------------

The following are the primary assumptions made regarding this application.

	1. Although the function to select the payer also reports the total order cost for the day,
		since the primary focus is on determining who will pay out of the group, and no definitive
		context on where this group will be getting coffee was provided, I assumed that currency units
		were superfluous for the solution and chose to keep my numbers unitless, omitting any currency
		indicators for the cost and balance details, including when the total cost is reported. If
		currency indicators were needed or desired, they could easily be added with minimal changes.

	2. I also assumed general good faith between the participants (and anyone else that may
		have access to the app). As such, the save file is maintained as a plain text file, which
		can be easily read and altered (including maliciously, if desired, to make some users pay
		more frequently and others less so or not at all). Similarly, there is no security behind
		actions like altering the account balance or order cost of members, or running the application
		in general. If good faith of the actors involved can not be assumed, changes would be made
		such as encrypting the save file, and perhaps setting up a password on initial startup to
		help avoid bad actors altering things (though even then they could just break or delete the
		save file, which is another reason the original assumption was made).

	3. Although the prompt specifically limits the context of the problem to a group of 7 members
		that always get coffee and always get the same thing. I designed the application under the
		assumption that in the context of the prompt the participants may not assume none of them will
		ever change their order, or that no members will ever leave or be added. As such, I built the
		application with functions to accomodate the addition of more users, the removal of users, and
		updates to the cost of a member's order or even of their ongoing account balance. While these
		functions are likely beyond the scope of the prompt, they seemed necessary to provide at least
		basic end-to-end functionality in a real world sense.

	4. My last major assumption is that user's may (unintentionally) close or crash the program at any time,
		so to keep the list of members persistent in such cases, the application saves the list to file any
		time it is altered. As a result, a corresponding assumption is made that the list will never be so
		large as to cause performance or other issues related to re-writing a large file from scratch with
		every function that alters the list details.
