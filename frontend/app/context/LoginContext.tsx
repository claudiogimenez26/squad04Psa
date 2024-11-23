"use client";

import { createContext, useReducer } from "react";
import { Recurso } from "@/app/lib/types";

type LoginAction = { type: "ACTUALIZAR_RECURSO"; payload: Recurso };

export const LoginContext = createContext<{
  state: Recurso;
  dispatch: React.Dispatch<LoginAction>;
} | null>(null);

export function loginContextReducer(state: Recurso, action: LoginAction) {
  switch (action.type) {
    case "ACTUALIZAR_RECURSO":
      return action.payload;
    default:
      return state;
  }
}

export function LoginContextProvider({
  recurso,
  children,
}: {
  recurso: Recurso;
  children: React.ReactNode;
}) {
  const [state, dispatch] = useReducer(loginContextReducer, recurso);

  return (
    <LoginContext.Provider value={{ state, dispatch }}>
      {children}
    </LoginContext.Provider>
  );
}
