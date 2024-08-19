import { Router } from "express";
import { loginUser } from "../controllers/authController";
import { verifyToken } from "../middleware/authMiddleware";

const router = Router();

router.post("/login", loginUser);
router.get("/validate-token", verifyToken, (req, res) => {
  res.status(200).json({ message: "Token is valid", user: req.user });
});

export default router;
