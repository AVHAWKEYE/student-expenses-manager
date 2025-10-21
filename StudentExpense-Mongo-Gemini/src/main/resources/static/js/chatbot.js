function sendMessage() {
    let input = document.getElementById('userInput').value;
    let chatbox = document.getElementById('chatbox');
    chatbox.innerHTML += "<p><b>You:</b> " + input + "</p>";
    document.getElementById('userInput').value = '';
    chatbox.innerHTML += "<p><b>Bot:</b> Hello! I am your assistant.</p>";
}
