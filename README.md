How to Run
Start the Server:

bash
Copy code
java Server
Start the Client:

bash
Copy code
java Client
Enter the admin password (123) for full access, or proceed with limited privileges.

Example Commands:
sendmsg: Send an encrypted message.
readfile: Read a file.
createfile: Create a new file (Admin-only).
Folder Structure
arduino
Copy code
src/
├── Client.java         # Client-side code
├── Server.java         # Server-side code
├── AESEncryption.java  # Handles AES encryption
├── AESDecryption.java  # Handles AES decryption
sharedFolder/           # Server's shared directory
