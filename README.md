# Timeline Trivia

## General Idea

Design an app that takes in comunity trivia questions

## Project Structure

Created in Android Studio. Major project file structure

 - Java Base Files
    1. app
    2. src
    3. main
    4. java
    5. com
    6. example
    7. timeline_trivia
        - MainActivity > The main java class that runs the main page and creates the other classes
        - QuestionMaster > DataStructure and storage for saving, asking, and checking question answer pairs
            - Much of this runs in constant time via the use of cross referencing datastructures (IE. Some delete in constant time, some return the last input in constant time, etc.)
            - The trick here is maintaining all cross references as updates are made to the project
 - Android App Layout (XML)
    1. app
    2. src
    3. main
    4. res
    5. layout
        - activity_main > Currently the only working page in the project

## TODO short(ish) term
    - [x] Verify Code can work on other machines
         -kb: the app runs, and buttons are displayed. Questions do not always display, I added a task for this in Trello. 

    - [~] General file cleanup and gitignore population
         -kb: I made a /proj directory to track any project management stuff, which I'm including this under. Please review the file_cleanup.txt file to see how much I guessed right.
               I don't think any of the gradle files need to be excluded or cleaned, outside the generated /app/build files. 