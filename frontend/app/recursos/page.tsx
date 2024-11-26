import { encodearFecha } from "@/_lib/fecha";
import { redirect } from "next/navigation";

export default async function () {
  const data = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos = await data.json();

  redirect(`/recursos/${recursos[0].id}?fecha=${encodearFecha(new Date())}`);
}