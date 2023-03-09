import React from "react";
import { createBrowserRouter } from "react-router-dom";
import Root from "@/pages/Root";
import SignUpPage from "./pages/Auth/SingUpPage";
import LoginPage from "./pages/Auth/LoginPage";
import MainPage from "@/pages/MainPage";

const router = createBrowserRouter([
  { path: "/", element: <Root /> },
  { path: "/signup", element: <SignUpPage /> },
  { path: "/login", element: <LoginPage /> },
  { path: "/main", element: <MainPage /> }
]);


export default router;
