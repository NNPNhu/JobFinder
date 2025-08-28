// import { useRef, useState } from "react";
// import { Alert, Button, FloatingLabel, Form } from "react-bootstrap";
// import Apis, { authApis, endpoints } from "../configs/Apis";
// import { useNavigate } from "react-router-dom";
// import MySpinner from "./layout/MySpinner";

// const Apply = () => {
//     const info = [{
//         label: "Thư giới thiệu",
//         type: "text",
//         field: "coverLetter"
//     }];
//     const avatar = useRef();
//     const [msg, setMsg] = useState("")

//     const [jobapplication, setJobapplication] = useState({});
//     const [loading, setLoading] = useState(false);
//     const nav = useNavigate();

//     const setState = (value, field) => {
//         setJobapplication({...jobapplication, [field]: value});
//     }

//     const apply = async (e) => {
//         e.preventDefault();

        
//             try {
//                 setLoading(true);
//                 let form = new FormData();
//                 for (let f of info)
//                     if (f.field !== 'confirm') {
//                         form.append(f.field, jobapplication[f.field]);
//                     }
    
//                 form.append('resumeLink', resumeLink.current.files[0]);
//                 console.info(form);
//                 let res = await authApis.post(endpoints['apply'], form, {
//                     headers: {
//                         'Content-Type': 'multipart/form-data'
//                     }
//                 });
//                 if (res.status === 201)
//                     nav("/");
//             } catch {

//             } finally {
//                 setLoading(false);
//             }
//         }
//     }

//     return (
//         <>
//             <h1 className="text-center text-success mt-1">Ứng tuyển</h1>

//             {msg && <Alert variant="danger">{msg}</Alert>}

//             <Form onSubmit={apply}>
//                 {info.map(f => <FloatingLabel key={f.field} controlId="floatingInput" label={f.label} className="mb-3">
//                     <Form.Control type={f.type} placeholder={f.label} required value={jobapplication[f.field]} onChange={e => setState(e.target.value, f.field)} />
//                 </FloatingLabel>)}

//                 <FloatingLabel controlId="floatingInput" label="CV của bạn" className="mb-3">
//                     <Form.Control type="file" placeholder="CV" ref={avatar} />
//                 </FloatingLabel>

//                 {loading ? <MySpinner />:<Button type="submit" variant="success" className="mt-1 mb-1">Gửi</Button>}
                
//             </Form>
//         </>
//     )


// export default Apply;
import React, { useState } from 'react';
import Api from '../configs/Apis';  // Giả sử bạn có Api để gọi axios
import cookie from 'react-cookies';

const ApplyJobForm = ({ jobId }) => {
  const [coverLetter, setCoverLetter] = useState('');
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    if (!coverLetter.trim()) {
      setError('Vui lòng nhập thư xin việc.');
      return;
    }
    if (!file) {
      setError('Vui lòng chọn file hồ sơ.');
      return;
    }

    setLoading(true);
    try {
      const formData = new FormData();
      formData.append('coverLetter', coverLetter);
      formData.append('file', file);
      // Truyền jobId dưới dạng jobId.id theo backend @ModelAttribute
      // Nếu backend cần đúng cấu trúc jobId.id, ta gửi như sau:
      formData.append('jobId.id', jobId);

      const res = await Api.post('/addapplication', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${cookie.load('token')}`,
        },
      });

      if (res.status === 200) {
        setSuccess('Gửi đơn ứng tuyển thành công!');
        setCoverLetter('');
        setFile(null);
      } else {
        setError('Gửi đơn ứng tuyển thất bại.');
      }
    } catch (ex) {
      setError('Có lỗi xảy ra khi gửi đơn.');
      console.error(ex);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-md mx-auto p-4 border rounded">
      <h3 className="mb-4 font-semibold">Ứng tuyển công việc</h3>

      {error && <p className="text-red-600 mb-2">{error}</p>}
      {success && <p className="text-green-600 mb-2">{success}</p>}

      <div className="mb-3">
        <label htmlFor="coverLetter" className="block mb-1 font-medium">
          Thư xin việc
        </label>
        <textarea
          id="coverLetter"
          value={coverLetter}
          onChange={(e) => setCoverLetter(e.target.value)}
          rows={5}
          className="w-full border px-2 py-1"
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="file" className="block mb-1 font-medium">
          Hồ sơ (CV)
        </label>
        <input
          type="file"
          id="file"
          onChange={(e) => setFile(e.target.files[0])}
          accept=".pdf,.doc,.docx"
          required
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        {loading ? 'Đang gửi...' : 'Gửi ứng tuyển'}
      </button>
    </form>
  );
};

export default ApplyJobForm;
