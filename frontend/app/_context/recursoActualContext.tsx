"use client";

import { CargasDeHorasPorRecurso, Recurso } from "@/_lib/tipos";
import {
  createContext,
  Dispatch,
  useEffect,
  useReducer,
  useState
} from "react";

type State = {
  recursos: Recurso[];
  recursoActual: Recurso | null;
  cargas: CargasDeHorasPorRecurso;
};
type Action = { payload: Recurso };

export const RecursoActualContext = createContext<{
  state: State;
  dispatch: Dispatch<Action>;
} | null>(null);

function reducer(state: State, action: Action): State {
  window.localStorage.setItem("recursoActual", JSON.stringify(action.payload));
  return { ...state, recursoActual: action.payload };
}

export default function RecursoActualContextProvider({
  recursos,
  cargas,
  children
}: {
  recursos: Recurso[];
  cargas: CargasDeHorasPorRecurso;
  children: React.ReactNode;
}) {
  const [state, dispatch] = useReducer(reducer, {
    recursos,
    cargas,
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
    <RecursoActualContext.Provider value={{ state, dispatch }}>
      {children}
    </RecursoActualContext.Provider>
  );
}
