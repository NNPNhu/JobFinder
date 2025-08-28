// import React from 'react';

// const JobCard = ({ job }) => {
//   return (
//     <>
//       <div className="bg-white rounded-lg shadow-md p-4 m-2 flex flex-col items-start w-full max-w-sm border border-gray-200">
//         <div className="flex items-center mb-2">
//           {/* {job.companyId?.userAvatar ? (
//             <img
//               src={job.companyId.userAvatar || 'https://via.placeholder.com/40'} // Ảnh avatar, dùng placeholder nếu không có
//               alt={`${job.title} Avatar`}
//               className="w-10 h-10 rounded-full mr-2"
//               onError={(e) => {
//                 console.log(`Failed to load avatar: ${job.companyId.userAvatar}`);
//                 e.target.src = 'https://via.placeholder.com/50?text=No+Image';
//             }}
//             />) : (
//             <p>Không có avatar</p>
//           )} */}
//           <div>
//             <h3 className="text-md font-semibold text-gray-800">{job.company}</h3>
//             <p className="text-sm text-gray-600">{job.title}</p>
//           </div>
//         </div>
//         <p className="text-sm text-gray-700 mb-2">{job.description}</p>
//         <div className="text-sm text-gray-600 mb-1">
//           <span>Lương: {job.salaryFrom.toLocaleString()} - {job.salaryTo.toLocaleString()} triệu</span>
//         </div>
//         <div className="text-sm text-gray-600 mb-1">
//           <span>Địa điểm: {job.location}</span>
//         </div>
//         <div className="text-sm text-gray-600 mb-1">
//           <span>Thời gian: {job.workingTime}</span>
//         </div>
//         <div className="flex justify-between w-full mt-2">
//           <button className="text-blue-500 text-sm hover:underline">Xem chi tiết</button>
//           <button className="text-green-500 text-sm hover:text-green-700">
//             <span role="img" aria-label="like">❤️</span> Yêu thích
//           </button>
//         </div>
//       </div>
//     </>
//   );
// };

import React, { useState } from 'react';
import ApplyJobForm from './ApplyJobForm';


const JobCard = ({ job }) => {
  const [showApplyForm, setShowApplyForm] = useState(false);

  return (
    <div className="bg-white rounded-lg shadow-md p-4 m-2 flex flex-col items-start w-full max-w-sm border border-gray-200">
      <div className="flex items-center mb-2">
        <div>
          <h3 className="text-md font-semibold text-gray-800">{job.company}</h3>
          <p className="text-sm text-gray-600">{job.title}</p>
        </div>
      </div>
      <p className="text-sm text-gray-700 mb-2">{job.description}</p>
      <div className="text-sm text-gray-600 mb-1">
        <span>Lương: {job.salaryFrom.toLocaleString()} - {job.salaryTo.toLocaleString()} triệu</span>
      </div>
      <div className="text-sm text-gray-600 mb-1">
        <span>Địa điểm: {job.location}</span>
      </div>
      <div className="text-sm text-gray-600 mb-1">
        <span>Thời gian: {job.workingTime}</span>
      </div>
      <div className="flex justify-between w-full mt-2">
        <button
          className="text-blue-500 text-sm hover:underline"
          onClick={() => setShowApplyForm(!showApplyForm)}
        >
          {showApplyForm ? 'Đóng form ứng tuyển' : 'Ứng tuyển'}
        </button>
        <button className="text-green-500 text-sm hover:text-green-700">
          <span role="img" aria-label="like">❤️</span> Yêu thích
        </button>
      </div>

      {/* Hiển thị form ứng tuyển khi người dùng bấm nút */}
      {showApplyForm && <ApplyJobForm jobId={job.id} />}
    </div>
  );
};


export default JobCard;