Points:
1-	Since no access to linux, I could not run the code on it. 
2-	Only tested the code by some simple inputs and the provided sample on the test folder. When I was running it by debug mode, it was correct but when I run it by run_tests.sh all three tests were failed!!!  I could not download the stream_payment from dropbox so cannot test the real output!!!
3-	I would be thankful if just run the code by a developer tool such as Eclipse. I am sure if there is any issue it just happens since of mismatch on shell script.  
4-	 Used the graph structure and build the graph structure by the help of HashMap. 
5-	Did not consider the time-> supposed all records at stream_payment.txt are happen after batch_payment. 

The main Idea: 
 
1-	First, check if two persons are friend-> check the first friendship relation on the graph. Using the Hashmap causes to have good time.
2-	Secondly checked the is there any overlap between first children of both : first person and second person
3-	Third, check the overlap of the friend of friend of both first and second person. I move from both side to find the shared connection between two person which causing to have even less than Log(n) run time. 

