import React, { createContext, ReactNode, useEffect, useState } from "react";
import axios from "axios";

interface AuthContextType {
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  useEffect(() => {
    const validateToken = async () => {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const response = await axios.get(
            `process.env.REACT_APP_API_URL}/api/auth/validate-token`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          setIsAuthenticated(response.data === 200);
        } catch (error) {
          console.error("Invalid or expired token:", error);
          localStorage.removeItem("token");
          setIsAuthenticated(false);
        }
      }
    };

    validateToken();
  }, []);

  const login = async (email: string, password: string) => {
    const response = await axios.post(
      `${process.env.REACT_APP_API_URL}/api/auth/login`,
      { email, password }
    );
    const { token } = response.data;
    localStorage.setItem("token", token);
    setIsAuthenticated(true);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
