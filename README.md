# LocalAI Chatbot — Java + Ollama + Llama 3.2

A free, locally running AI chatbot built using Java and Ollama, powered by the Llama 3.2 large language model.

The application allows users to interact with an AI assistant through a command-line interface. It supports conversational memory, allowing the chatbot to remember previous messages during the current session.

Unlike cloud-based AI chatbots, this project runs the language model locally and does not require a paid API key.

## Features

- **Local AI Integration:** Uses Ollama to run the Llama 3.2 model locally.
- **Conversational Memory:** Maintains conversation history during the current session.
- **Context-Aware Responses:** Uses previous messages to generate relevant responses.
- **Reset Conversation:** Clears the current conversation using the `reset` command.
- **Simple CLI Interface:** Interact with the chatbot directly through the terminal.
- **No API Key Required:** Does not require an Anthropic or other paid cloud API key.
- **Privacy-Friendly:** Conversations are sent to the local Ollama service rather than a cloud LLM API.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Application development |
| Java HttpClient | Communicating with the local AI service |
| Ollama | Running the LLM locally |
| Llama 3.2 | AI language model |
| JSON | Request and response communication |

## Project Architecture

```text
User
 |
 v
Java Console Application
 |
 v
ChatBot.java
 |
 | HTTP POST
 v
Ollama Local API
 |
 v
Llama 3.2 Model
 |
 v
AI-Generated Response
 |
 v
Java Console
```

## Prerequisites

Before running the application, make sure you have:

- Java JDK 11 or later
- Ollama installed
- Llama 3.2 model downloaded
- VS Code or another Java IDE

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/java-ollama-local-ai-chatbot.git
```

Navigate to the project directory:

```bash
cd java-ollama-local-ai-chatbot
```

### 2. Install Ollama

Download Ollama for Windows, macOS, or Linux:

https://ollama.com/download

### 3. Download the Llama 3.2 model

Open a terminal and run:

```bash
ollama pull llama3.2
```

Alternatively, start the model directly:

```bash
ollama run llama3.2
```

### 4. Compile the Java application

```bash
javac ChatBot.java
```

### 5. Run the chatbot

```bash
java ChatBot
```

The chatbot will start in the terminal.

## Usage

After launching the application, enter your message when prompted.

Example:

```text
================================
      FREE LOCAL AI CHATBOT
================================

Model: llama3.2
Powered by Ollama + Llama 3.2

Type 'exit' to quit.
Type 'reset' to clear memory.

You: Hello

Bot: Hello! How can I help you today?

You: My name is Sunita.

Bot: Nice to meet you, Sunita!

You: What is my name?

Bot: Your name is Sunita.
```

### Available Commands

| Command | Description |
|---|---|
| `reset` | Clears the current conversation history |
| `exit` | Exits the chatbot |

## Configuration

The application communicates with Ollama through its local HTTP API.

```java
private static final String API_URL =
        "http://localhost:11434/api/chat";

private static final String MODEL = "llama3.2";
```

You can change the model name to another model available in your Ollama installation.

## Privacy

This application sends prompts to the Ollama service running on the local machine.

No external cloud LLM API key is required.

The application maintains conversation history in memory during the current session. The history is cleared when the application is restarted.

## Limitations

- The current version uses a command-line interface.
- AI response speed depends on the system hardware.
- The application requires Ollama to be running.
- Conversation history is not permanently stored.
- The current JSON parsing implementation is minimal and intended for standard text responses.

## Future Enhancements

- Java Swing or JavaFX graphical user interface
- Persistent chat history using file storage or a database
- Multiple LLM model selection
- Streaming AI responses
- Markdown rendering
- Chat export functionality
- Improved JSON parsing using Jackson or Gson

## Learning Outcomes

This project demonstrates:

- Java object-oriented programming
- HTTP communication using Java HttpClient
- REST API integration
- JSON request and response handling
- Local LLM integration
- Conversation history management
- Exception handling
