Made by Rudolf Kučera && Richard Činčura 
for VUT FIT IJA (Seminář Java) 8.5.2017

INSTALL
------------------------------------------------------
1.  In folder lib/, extract the apache ant somewhere into your computer.
2.  Go to My Computer(Tento Počítač), right click (klikni pravým tlačítkom na myši) and choose Properties(Vlastnosti).
3.  In the left panel, choose Advanced System Settings(Rozšírené systémové nastavenia).
4.  Then click the Environment Variables(Premenné prostredia...).
5.  Edit the variable PATH in both, system and user variables.
6.  Add ; and path to the apache ant bin folder to the end of the path variable (something like ;C:\......\apache-ant-1.10.1\bin).
7.  Create a new (or update if exists) variable JAVA_HOME and set path to the java jdk included in the folder lib/
8.  Run the windows command line.
9.  Use command cd (change directory) and type in the path to the Solitaire folder.
10. Type ant run.
11. Enjoy XDDD

UNINSTALL
------------------------------------------------------
1.  Delete Solitaire folder.
2.  Remove the apache-ant/bin path from the PATH system variable.