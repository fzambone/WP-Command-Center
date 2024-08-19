import express from "express";
import bodyParser from "body-parser";
import userRoutes from "./routes/userRoutes";
import authRoutes from "./routes/authRoutes";

const app = express();

app.use(bodyParser.json());
app.use("/api/users", userRoutes);
app.use("/api/auth", authRoutes);

export default app;
