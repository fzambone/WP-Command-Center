import { Request, Response } from "express";
import { createUser } from "../services/userService";

export const registerUser = async (req: Request, res: Response) => {
  const { email, password, role, assignedSpaces } = req.body;

  try {
    const user = await createUser(email, password, role, assignedSpaces);
    res.status(201).json(user);
  } catch (error) {
    res.status(500).json({ message: "Failed to create user", error });
  }
};
