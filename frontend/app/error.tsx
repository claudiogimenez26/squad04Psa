"use client";

import { useEffect } from "react";

export default function Error({
  error
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error(error);
  }, [error]);

  return <div>Error interno de renderización</div>;
}
