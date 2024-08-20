import React from 'react';

interface Props {
  icon: React.ReactNode;
  titleText: string;
  contentText: string;
}

export default function Input({ icon, titleText, contentText }: Props) {
  return (
    <div className="bg-[#E5E7EB] w-[237px] h-[87px] rounded-3xl p-4 flex flex-row items-center text-[#0F172A] font-Inter">
      {icon}
      <div className="ml-4 flex flex-col items-start">
        <p className="font-semibold text-sm text-nowrap overflow-ellipsis">{titleText}</p>
        <p className="font-normal text-lg text-nowrap overflow-ellipsis">{contentText}</p>
      </div>
    </div>
  )
}
