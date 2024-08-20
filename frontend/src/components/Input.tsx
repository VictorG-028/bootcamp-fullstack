import React from 'react';

interface Props {
  children: React.ReactNode;
  type: string;
  required?: boolean;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function Input({ children, type, required, onChange }: Props) {
  return (
    <>
      <label htmlFor={type} className={`text-base font-normal`}>{children}</label>
      <input type={type} required={required} onChange={onChange} className={`mb-3 p-2 bg-zinc-100 rounded-md ring-1 ring-zinc-500 hover:ring-blue-600`} />
    </>
  )
}
