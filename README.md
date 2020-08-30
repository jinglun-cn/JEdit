# JEdit Change Requests

[JEdit](http://www.jedit.org/) is a mature programmer's text editor under the GNU General Public License version 2.0.  It is written in Java and runs on any operating system with Java support, including BSD, Linux, macOS and Windows.

This project consists of implementation of several change requests of JEdit.

---------------------

### 1. Allow submitting bug report from jEdit

In jEdit, a new menu item in the menu bar named ‘Submit Bug’ is implemented. When clicked, it opens a browser and takes the user directly to jEdit’s Sourceforge bug tracker (https://sourceforge.net/p/jedit/bugs/) , so they can submit a bug report.


### 2. Highlight lines in the hypersearch results based on type
In jEdit, terms searched for are highlighted in the lines displayed in the hypersearch results window.

Search matches are highlighted differently by the type of line where
the match is found (only support Java files):

-comment lines matches should be highlighted in green (any kind of comment
supported in Java: single line comments, multi-line comments, and
documentation comments)

-source code lines matches in red.

### 3. Filter modified-date for Search in Directory
In the Search in Directory feature (under menu Search/Search in Directory), implement a filter based on the modified date of a file.

In the search dialog, add two date fields, called "Modified from:" and "Modified to:" where the user can specify the dates to filter the files that match based on their last modified dates. Then implement the functionality of this new filter such that when selected, the search and replace functionality will be performed only in files that match the filter conditions (i.e., they were last modified between the dates listed in “Modified from:” and “Modified to:”).

