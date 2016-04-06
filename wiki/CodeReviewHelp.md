# How to do code review #

## Short Answer ##
1. Download this file: <br />
http://codereview.appspot.com/static/upload.py (make sure you have python 2.5+ installed, and include it in your path)
<br /><br />

2. Here is a sample from Michael to send invitations:<br />
upload.py -r omnidroid-devel@googlegroups.com --send\_mail -m "Removed workspace warnings and added svn:ignore values. Fixes [issue #4](https://code.google.com/p/omnidroid/issues/detail?id=#4)."
<br /><br />

3. Reviewers come and mark it up with suggestions.

4. Coder then addresses those comments in the code, runs upload.py again (this time
including -i ##### so it patches an existing code review and doesn't create a new
one), and then address EACH comment in the codereview tool.  Most of them
will probably just be "done" but sometimes the coder might disagree so they'll reply
with my opinion.

5. The reviewers either point out more issues to address, in which case the coder
repeats the patch cycle, or they give the coder the go ahead to submit.  Once the coder has addressed all the reviewers comments and has at least one approval from a reviewer, the coder can do an svn update (in case anything has changed) and then a commit.

## Long Answer ##
Here is a long document: http://code.google.com/p/rietveld/wiki/CodeReviewHelp
<br /><br />

## Extra Help, Step By Step ##
This is a sample walk through on windows, if it helps:

1) Pull out the source code using svn to some folder, say C:\omnidroid\

2) Make your changes to the source files.

3) Now you're ready to do a code review, make sure you have upload.py, I have mine in C:\lib\python\upload.py

4) Open up a cmd prompt, navigate to the folder that contains the files you changed. For instance, I may have edited only Test.java in this folder:

> C:\omnidroid\a\b\c\Test.java

so I will do:

> > cd C:\omnidroid\a\b\c\

5) Run svn stat here to see what diffs exist in your working directory:

> > svn stat

you want to only have diffs that relate to the issue you're interested in doing a code review for. If you have also edited Test2.java, but it has nothing to do with your edits for Test.java, you probably want to revert Test2.java so as not to unintentionally mix diffs from different issues.

6) Now you can run upload.py here. It will automatically use the diffs in your working directory to associate with the code review (this is why we did a preliminary check in #5):

> > C:\lib\python\upload.py -r omnidroid-devel@googlegroups.com --send\_mail -m "My description about these changes, related to [issue #123](https://code.google.com/p/omnidroid/issues/detail?id=#123)"

7) The python script will make a new code review entry for you. It should generate a new code review ID number (this is not the same number as the issue #). When you make subsequent changes for this code review, then you use the -i flag and pass in that code review ID number to upload.py in order to keep all the changes organized.

8) An email should go out to the dev group, wait for others to reply.