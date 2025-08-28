import React, { useState } from 'react';
import axios from 'axios';
import Apis, { authApis, endpoints } from "../configs/Apis";

const ApplyJobForm = ({ jobId }) => {
  const [coverLetter, setCoverLetter] = useState('');
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!jobId) {
      setMessage("Không có jobId để gửi đơn ứng tuyển.");
      return;
    }
    if (!file) {
      setMessage("Vui lòng chọn file CV.");
      return;
    }

    const formData = new FormData();
    formData.append('coverLetter', coverLetter);
    formData.append('file', file);
    formData.append('jobId.id', jobId);  // Vì backend dùng @ModelAttribute và nested object

    try {
      setLoading(true);
      const res = await authApis().post('addapplication', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setMessage('Ứng tuyển thành công!');
    } catch (err) {
      setMessage('Lỗi khi gửi đơn ứng tuyển.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-4 border rounded bg-white">
      <div>
        <label>Thư xin việc:</label>
        <textarea
          value={coverLetter}
          onChange={(e) => setCoverLetter(e.target.value)}
          rows={4}
          className="w-full border rounded p-2"
        />
      </div>
      <div className="mt-2">
        <label>File CV (PDF, DOC, JPG):</label>
        <input
          type="file"
          accept=".pdf,.doc,.docx"
          onChange={(e) => setFile(e.target.files[0])}
        />
      </div>
      <button
        type="submit"
        disabled={loading}
        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded"
      >
        {loading ? 'Đang gửi...' : 'Nộp đơn'}
      </button>
      {message && <p className="mt-2 text-sm text-red-500">{message}</p>}
    </form>
  );
};

export default ApplyJobForm;
