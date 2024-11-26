import { redirect } from "next/navigation";

export const experimental_ppr = true;

export default async function () {
  const data = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos = await data.json();

  redirect(`/recursos/${recursos[0].id}`);
}
