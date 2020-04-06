package com.tamagotchi.tamagotchiserverprotocol;

class ServerFaultException extends Exception {
    ServerFaultException(String errorMessage) {
        super(errorMessage);
    }
}
