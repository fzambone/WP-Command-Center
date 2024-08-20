import { db, auth } from "../admin";
import { User } from "../models/userModel";

export const createUser = async (
  email: string,
  password: string,
  role: string,
  assignedSpaces: string[] = []
) => {
  try {
    const userRecord = await auth.createUser({
      email,
      password,
    });

    await auth.setCustomUserClaims(userRecord.uid, {
      role: role,
      assignedSpaces: assignedSpaces,
    });

    const user: User = {
      uid: userRecord.uid,
      email: email,
      role: role as "super-admin" | "space-admin" | "regular-user",
      assignedSpaces: assignedSpaces,
    };
    await db.collection("users").doc(user.uid).set(user);

    return user;
  } catch (error) {
    console.error("Error creating user: ", error);
    throw error;
  }
};
