| Application | Event/Action | Parameter | Data Type | Event | Action |
|:------------|:-------------|:----------|:----------|:------|:-------|
| **SMS**     |              |           |           |       |        |
|             | Send         |           |           |       | X      |
|             |              |  Number   | PhoneNumber |       |        |
|             |              |  Message  | Text      |       |        |
|             | Sent         |           |           | X     |        |
|             |              | Number    | PhoneNumber |       |        |
|             |              | Message   | Text      |       |        |
|             | Received     |           |           | X     |        |
|             |              | Number    | PhoneNumber |       |        |
|             |              | Message   | Text      |       |        |
| **Email**   |              |           |           |       |        |
|             | Send         |           |           |       | X      |
|             |              |  To       | EmailList |       |        |
|             |              | From      | Email     |       |        |
|             |              | Cc        | EmailList |       |        |
|             |              | Bcc       | EmailList |       |        |
|             |              | Subject   | Text      |       |        |
|             |              | Message   | Text      |       |        |
|             | Sent         |           |           | X     |        |
|             |              |  To       | EmailList |       |        |
|             |              | From      | Email     |       |        |
|             |              | Cc        | EmailList |       |        |
|             |              | Bcc       | EmailList |       |        |
|             |              | Subject   | Text      |       |        |
|             |              | Message   | Text      |       |        |
|             | Received     |           |           | X     |        |
|             |              |  To       | EmailList |       |        |
|             |              | From      | Email     |       |        |
|             |              | Cc        | EmailList |       |        |
|             |              | Bcc       | EmailList |       |        |
|             |              | Subject   | Text      |       |        |
|             |              | Message   | Text      |       |        |
| **Phone**   |              |           |           |       |        |
|             | DialNumber   |           |           |       | X      |
|             |              |  Number   | PhoneNumber |       |        |
|             | IgnoreCall   |           |           |       | X      |
|             | Dialed       |           |           | X     |        |
|             |              | Number    | PhoneNumber |       |        |
|             | Received     |           |           | X     |        |
|             |              | Number    | PhoneNumber |       |        |