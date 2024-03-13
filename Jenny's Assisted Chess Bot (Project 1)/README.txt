Jenny Spicer / January 18, 2024 / Intro to AI (4525) Project 1: Chess Bot

For editing:
Make sure you have Eclipse downloaded and installed.
• Open Eclipse.
• File > Import
• Under the General category, choose Existing Projects into Workspace and
click Next.
• Choose the Select archive file option.
• Browse for ChessBot.zip
• Click Finish

-----------------------

Run a sample tournament in the terminal:
java -jar chess.jar 2 bots/random.jar bots/greedy.jar bots/novice.jar

Broken down...
java -jar chess.jar ß This part is simply saying “run chess.jar”
• 2 ß This can be any positive integer. It is how many games each bot should play
against each other, alternating white and black (i.e., who goes first).
• bots/random.jar bots/greedy.jar bots/novice.jar ß This is you
specifying which bots you want participating in the tournament. Specifying the
bots/ is assuming the bot jar files are in a directory called bots, which itself is in
the same directory as chess.jar (which should be the case if you followed the
download instructions above).

-----------------------

