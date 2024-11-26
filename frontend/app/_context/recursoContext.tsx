"use client";

import { Recurso } from "@/_lib/tipos";
import {
  createContext,
  Dispatch,
  useEffect,
  useReducer,
  useState
} from "react";

type State = { recursos: Recurso[]; recursoActual: Recurso | null };
type Action = { payload: Recurso };

export const RecursoContext = createContext<{
  state: State;
  dispatch: Dispatch<Action>;
} | null>(null);

function reducer(state: State, action: Action): State {
  window.localStorage.setItem("recursoActual", JSON.stringify(action.payload));
  return { ...state, recursoActual: action.payload };
}

export default function RecursoContextProvider({
  recursos,
  children
}: {
  recursos: Recurso[];
  children: React.ReactNode;
}) {
  const [state, dispatch] = useReducer(reducer, {
    recursos,
    recursoActual: recursos[0]
  });

  const [recursoCargado, setRecursoCargado] = useState(false);

  useEffect(() => {
    setRecursoCargado(true);

    const recursoGuardado = window.localStorage.getItem("recursoActual");

    if (recursoGuardado) {
      dispatch({ payload: JSON.parse(recursoGuardado) });
    }
  }, []);

  if (!recursoCargado) {
    return null;
  }

  return (
    <RecursoContext.Provider value={{ state, dispatch }}>
      {children}
    </RecursoContext.Provider>
  );
}
