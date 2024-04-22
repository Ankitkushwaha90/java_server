from flask import Flask, render_template
from pyngrok import ngrok

# Set your Ngrok authtoken here
ngrok.set_auth_token("your_ngrok_authtoken")

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

if __name__ == '__main__':
    # Start ngrok tunnel
    public_url = ngrok.connect(addr='127.0.0.1:8000')

    print(" * Running on ", public_url)

    app.run(host='127.0.0.1', port=8000)
