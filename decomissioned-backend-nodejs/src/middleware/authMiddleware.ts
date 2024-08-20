import { NextFunction, Request, Response } from "express";
import { getAuth } from "firebase-admin/auth";
import "../types/express";

export const verifyToken = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  const token = req.headers.authorization?.split(" ")[1];

  if (!token) {
    return res.status(401).json({ message: "No token provided." });
  }

  try {
    const decodedToken = await getAuth().verifyIdToken(token);
    req.user = decodedToken;
    next();
  } catch (error) {
    const err = error as Error;
    return res
      .status(401)
      .json({ message: "Invalid token", error: err.message });
  }
};
