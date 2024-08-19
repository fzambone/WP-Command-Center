import { Request, Response } from "express";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { initializeApp } from "firebase/app";
import { firebaseConfig } from "../config/firebaseConfig";

export const loginUser = async (req: Request, res: Response) => {
  const { email, password } = req.body;

  const firebaseApp = initializeApp(firebaseConfig);
  const auth = getAuth(firebaseApp);

  if (!email || !password) {
    return res
      .status(400)
      .json({ message: "Email and password are required." });
  }

  try {
    const userCredential = await signInWithEmailAndPassword(
      auth,
      email,
      password
    );
    const idToken = await userCredential.user.getIdToken();

    res.status(200).json({ token: idToken });
  } catch (error) {
    res.status(500).json({ message: "Error generating token", error: error });
  }
};
