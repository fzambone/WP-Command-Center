import React from "react";
import { Button, Container } from "react-bootstrap";
import { useAuth } from "../context/AuthContext";
import { auth } from "../firebaseConfig";
import { signOut } from "firebase/auth";
import { Navigate, useNavigate } from "react-router-dom";

const HomePage: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/login");
    } catch (error) {
      console.error("Error signing out: ", error);
    }
  };

  return (
    <Container className="mt-5">
      <h1>Welcome, {user?.email}!</h1>
      <p>You are logged in.</p>
      <Button variant="danger" onClick={handleLogout}>
        Log Out
      </Button>
    </Container>
  );
};

export default HomePage;
