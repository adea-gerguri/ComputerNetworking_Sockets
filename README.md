# Computer Networking Sockets Project

#### University of Prishtina - Faculty of Electrical and Computer Engineering

### Supervisor

This is the second project from Computer Networks course assigned and supervised by PhD.C Mërgim Hoti.

## Usage

This project was developed in Java and implements a simple client-server model using UDP sockets. The client can send encrypted messages and perform file operations on the server.

## Features
### Client:
- Send encrypted messages using AES encryption.
- File operations:
  - **Read**: View file content.
  - **Write**: Add content to a file (Admin-only).
  - **Create**: Create files or folders (Admin-only).
  - **Delete**: Delete files or folders (Admin-only).
  - **Execute**: Open files in Notepad on the server (Admin-only).
### Server:
- Process client requests.
- Manage files in the `sharedFolder` directory.
- Decrypt and display encrypted messages.

## How to Run

1. *Start the Server*:
   java Server
   

2. *Start the Client*:
   java Client
   

3. Enter the admin password (123) for full access, or proceed with limited privileges.

### Example Commands:
- sendmsg: Send an encrypted message.
- readfile: Read a file.
- createfile: Create a new file (Admin-only).

## Folder Structure

src/
├── Client.java         # Client-side code
├── Server.java         # Server-side code
├── AESEncryption.java  # Handles AES encryption
├── AESDecryption.java  # Handles AES decryption
sharedFolder/           # Server's shared directory

# Contributors

#### - Adea Tabaku

#### - Adea Gerguri

#### - Adea Lluhani

#### - Vlera Islami
