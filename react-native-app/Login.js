import { StatusBar } from "expo-status-bar";
import React, { useState } from "react";
import {
  StyleSheet,
  Text,
  View,
  Image,
  TextInput,
  Button,
  TouchableOpacity,
} from "react-native";
import { showMessage } from "react-native-flash-message";
import { useShareableState } from './ShareableState';
 
export default function Login() {
  const [ email, setEmail ] = useState("");
  const [ password, setPassword ] = useState("");
  const [ headers, setHeaders ] = useState([]);
  const { setJwt, setAutenticated } = useShareableState();

  const onLogin = () => {
      fetch(`${process.env.API_GATEWAY_URL}/api/authenticatedUser`)
        .then((response) => setHeaders(response.headers))
        .catch((error) => {
          console.log("Something went wrong: ", error);
        });

      const body = "username=" + encodeURIComponent(email) + '&password=' + encodeURIComponent(password);
      fetch(`${process.env.API_GATEWAY_URL}/api/authenticatedUser`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: body
        })
        .then((response) => response.json())
        .then((responseJson) => {
            console.log("response: ", responseJson);
            if (responseJson.status) {
                showMessage({
                    message: "Something went wrong",
                    description: responseJson.message,
                    type: "danger",
                    icon: "auto"
                });
            } else {
                setJwt(responseJson);
                setAutenticated(true);
            }
        })
        .catch((error) => {
          console.log("Something went wrong: ", error);
        });
    showMessage({
        message: `Credentials, ${email} + ${password}`,
        type: "success",
        icon: "auto"
    });
  }
 
  return (
    <View style={styles.container}>
      <Image style={styles.image} source={require("./assets/logo.png")} />
 
      <StatusBar style="auto" />
      <View style={styles.inputView}>
        <TextInput
          style={styles.TextInput}
          placeholder="Email."
          placeholderTextColor="#003f5c"
          onChangeText={(email) => setEmail(email)}
        />
      </View>
 
      <View style={styles.inputView}>
        <TextInput
          style={styles.TextInput}
          placeholder="Password."
          placeholderTextColor="#003f5c"
          secureTextEntry={true}
          onChangeText={(password) => setPassword(password)}
        />
      </View>
 
      <TouchableOpacity>
        <Text style={styles.forgot_button}>Forgot Password?</Text>
      </TouchableOpacity>
 
      <TouchableOpacity style={styles.loginBtn} onPress={() => onLogin()}>
        <Text style={styles.loginText}>LOGIN</Text>
      </TouchableOpacity>
    </View>
  );
}
 
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
 
  image: {
    marginBottom: 40,
  },
 
  inputView: {
    backgroundColor: "#FFC0CB",
    borderRadius: 30,
    width: "70%",
    height: 45,
    marginBottom: 20,
 
    alignItems: "center",
  },
 
  TextInput: {
    height: 50,
    flex: 1,
    padding: 10,
    marginLeft: 20,
  },
 
  forgot_button: {
    height: 30,
    marginBottom: 30,
  },
 
  loginBtn: {
    width: "80%",
    borderRadius: 25,
    height: 50,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 40,
    backgroundColor: "#FF1493",
  },
});