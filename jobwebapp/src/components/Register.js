import React, { useState } from 'react';
import { Button, Form, Col, Row, Alert, Container } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { endpoints } from '../configs/Apis';
import cookie from 'react-cookies';

const Register = () => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        username: '',
        password: '',
        role: 'CANDIDATE',
        avatar: null,
        companyName: '',
        taxCode: '',
        description: '',
        address: '',
        website: '',
        image1: null,
        image2: null,
        image3: null,
    });
    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFileChange = (e) => {
        const { name, files } = e.target;
        setFormData({ ...formData, [name]: files[0] });
    };

    const toggleEmployerFields = () => {
        return formData.role === 'EMPLOYER';
    };

    const validateForm = () => {
        const newErrors = {};

        if (!formData.firstName || formData.firstName.trim() === '') newErrors.firstName = 'Vui lòng nhập họ';
        if (!formData.lastName || formData.lastName.trim() === '') newErrors.lastName = 'Vui lòng nhập tên';
        if (!formData.email || formData.email.trim() === '') {
            newErrors.email = 'Vui lòng nhập email';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Email không hợp lệ';
        }
        if (!formData.username || formData.username.trim() === '') newErrors.username = 'Vui lòng nhập tên đăng nhập';
        if (!formData.password || formData.password.trim() === '') newErrors.password = 'Vui lòng nhập mật khẩu';
        if (!formData.avatar) newErrors.avatar = 'Vui lòng chọn ảnh đại diện';

        if (formData.role === 'EMPLOYER') {
            if (!formData.companyName || formData.companyName.trim() === '') newErrors.companyName = 'Vui lòng nhập tên công ty';
            if (!formData.taxCode || formData.taxCode.trim() === '') newErrors.taxCode = 'Vui lòng nhập mã số thuế';
            if (!formData.image1) newErrors.image1 = 'Vui lòng chọn ảnh môi trường làm việc 1';
            if (!formData.image2) newErrors.image2 = 'Vui lòng chọn ảnh môi trường làm việc 2';
            if (!formData.image3) newErrors.image3 = 'Vui lòng chọn ảnh môi trường làm việc 3';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // Ngăn chặn hành vi mặc định của form (submit GET)
        const form = e.currentTarget;
        if (!form.checkValidity() || !validateForm()) {
          e.stopPropagation();
          form.classList.add('was-validated');
          return;
        }
      
        const data = new FormData();
        data.append('firstName', formData.firstName);
        data.append('lastName', formData.lastName);
        data.append('email', formData.email);
        data.append('phone', formData.phone);
        data.append('username', formData.username);
        data.append('password', formData.password);
        data.append('role', formData.role);
        if (formData.avatar) data.append('avatar', formData.avatar);
      
        if (formData.role === 'EMPLOYER') {
          data.append('companyName', formData.companyName);
          data.append('taxCode', formData.taxCode);
          data.append('description', formData.description);
          data.append('address', formData.address);
          data.append('website', formData.website);
          if (formData.image1) data.append('image1', formData.image1);
          if (formData.image2) data.append('image2', formData.image2);
          if (formData.image3) data.append('image3', formData.image3);
        }
      
        try {
          const response = await axios.post(endpoints.register, data, {
            headers: { 'Content-Type': 'multipart/form-data' },
          });
          if (response.data.success) {
            cookie.save('token', response.data.token, { path: '/' });
            setMessage('Đăng ký thành công! Vui lòng đăng nhập.');
            navigate('/login');
          } else {
            setMessage(response.data.message || 'Đăng ký thất bại.');
          }
        } catch (error) {
          console.error('Lỗi khi đăng ký:', error);
          setMessage(error.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.');
        }
    };

    return (
        <Container className="mt-5 d-flex justify-content-center">
            <div className="bg-white p-6 rounded-lg shadow-lg" style={{ maxWidth: '800px', width: '100%' }}>
                <h2 className="text-2xl font-bold text-success mb-2">Chào mừng bạn đến với JobSearch</h2>
                <p className="text-gray-600 mb-6">
                    Cùng xây dựng một hồ sơ nổi bật và nhận được các cơ hội sự nghiệp lý tưởng
                </p>
                <Form onSubmit={handleSubmit} className="space-y-4 needs-validation" noValidate>
                    <Row className="g-3">
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M10 10a4 4 0 100-8 4 4 0 000 8zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                                    </svg>
                                    <Form.Control
                                        type="text"
                                        name="firstName"
                                        placeholder="Họ"
                                        value={formData.firstName}
                                        onChange={handleInputChange}
                                        required
                                        isInvalid={!!errors.firstName}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.firstName}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M10 10a4 4 0 100-8 4 4 0 000 8zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                                    </svg>
                                    <Form.Control
                                        type="text"
                                        name="lastName"
                                        placeholder="Tên"
                                        value={formData.lastName}
                                        onChange={handleInputChange}
                                        required
                                        isInvalid={!!errors.lastName}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.lastName}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                                        <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
                                    </svg>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        placeholder="Nhập email"
                                        value={formData.email}
                                        onChange={handleInputChange}
                                        required
                                        isInvalid={!!errors.email}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
                                    </svg>
                                    <Form.Control
                                        type="text"
                                        name="phone"
                                        placeholder="Số điện thoại"
                                        value={formData.phone}
                                        onChange={handleInputChange}
                                    />
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M10 10a4 4 0 100-8 4 4 0 000 8zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                                    </svg>
                                    <Form.Control
                                        type="text"
                                        name="username"
                                        placeholder="Tên đăng nhập"
                                        value={formData.username}
                                        onChange={handleInputChange}
                                        required
                                        isInvalid={!!errors.username}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path fillRule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clipRule="evenodd" />
                                    </svg>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        placeholder="Nhập mật khẩu"
                                        value={formData.password}
                                        onChange={handleInputChange}
                                        required
                                        isInvalid={!!errors.password}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <Form.Select
                                    name="role"
                                    value={formData.role}
                                    onChange={handleInputChange}
                                    required
                                    isInvalid={!!errors.role}
                                >
                                    <option value="CANDIDATE">Ứng viên</option>
                                    <option value="EMPLOYER">Nhà tuyển dụng</option>
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">Vui lòng chọn vai trò.</Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                        <Col md={12}>
                            <Form.Group>
                                <div className="d-flex align-items-center border rounded p-2">
                                    <svg
                                        className="w-5 h-5 text-muted me-2"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 2H4v6l4-2 4 2 4-2V5z" />
                                    </svg>
                                    <Form.Control
                                        type="file"
                                        name="avatar"
                                        onChange={handleFileChange}
                                        accept="image/*"
                                        required
                                        isInvalid={!!errors.avatar}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.avatar}</Form.Control.Feedback>
                                </div>
                            </Form.Group>
                        </Col>

                        {/* Các trường dành cho EMPLOYER */}
                        {toggleEmployerFields() && (
                            <Row className="g-3">
                                <Col md={12}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path d="M2 4a2 2 0 012-2h12a2 2 0 012 2v12a2 2 0 01-2 2H4a2 2 0 01-2-2V4zm2 0v12h12V4H4z" />
                                            </svg>
                                            <Form.Control
                                                type="text"
                                                name="companyName"
                                                placeholder="Tên công ty"
                                                value={formData.companyName}
                                                onChange={handleInputChange}
                                                required
                                                isInvalid={!!errors.companyName}
                                            />
                                            <Form.Control.Feedback type="invalid">{errors.companyName}</Form.Control.Feedback>
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={12}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z" />
                                                <path fillRule="evenodd" d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 010 2H5a1 1 0 01-1-1zm5-1a1 1 0 100 2h1a1 1 0 100-2H9z" clipRule="evenodd" />
                                            </svg>
                                            <Form.Control
                                                type="text"
                                                name="taxCode"
                                                placeholder="Mã số thuế"
                                                value={formData.taxCode}
                                                onChange={handleInputChange}
                                                required
                                                isInvalid={!!errors.taxCode}
                                            />
                                            <Form.Control.Feedback type="invalid">{errors.taxCode}</Form.Control.Feedback>
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={12}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path fillRule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 0v12h8V4H6z" clipRule="evenodd" />
                                            </svg>
                                            <Form.Control
                                                as="textarea"
                                                name="description"
                                                placeholder="Mô tả công ty"
                                                value={formData.description}
                                                onChange={handleInputChange}
                                            />
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={12}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                                            </svg>
                                            <Form.Control
                                                type="text"
                                                name="address"
                                                placeholder="Địa chỉ"
                                                value={formData.address}
                                                onChange={handleInputChange}
                                            />
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={12}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path fillRule="evenodd" d="M4.083 9h1.946c.089-1.546.383-2.97.837-4.118A6.004 6.004 0 004.083 9zM10 2a8 8 0 100 16 8 8 0 000-16zm0 2c-.076 0-.232.032-.465.262-.238.234-.497.623-.737 1.182-.389.907-.673 2.142-.766 3.556h3.936c-.093-1.414-.377-2.649-.766-3.556-.24-.559-.5-.948-.737-1.182C10.232 4.032 10.076 4 10 4zm3.971 5c-.089-1.546-.383-2.97-.837-4.118A6.004 6.004 0 0115.917 9h-1.946zm-2.054 2H8.083c.093 1.414.377 2.649.766 3.556.24.559.5.948.737 1.182.233.23.389.262.465.262.076 0 .232-.032.465-.262.238-.234.497-.623.737-1.182.389-.907.673-2.142.766-3.556zm1.166 4.118c.454-1.147.748-2.572.837-4.118h1.946a6.004 6.004 0 01-2.783 4.118zm-6.268 0C6.412 13.97 6.118 12.546 6.03 11H4.083a6.004 6.004 0 002.783 4.118z" clipRule="evenodd" />
                                            </svg>
                                            <Form.Control
                                                type="url"
                                                name="website"
                                                placeholder="Website"
                                                value={formData.website}
                                                onChange={handleInputChange}
                                            />
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={4}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 2H4v6l4-2 4 2 4-2V5z" />
                                            </svg>
                                            <Form.Control
                                                type="file"
                                                name="image1"
                                                onChange={handleFileChange}
                                                accept="image/*"
                                                required
                                                isInvalid={!!errors.image1}
                                            />
                                            <Form.Control.Feedback type="invalid">{errors.image1}</Form.Control.Feedback>
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={4}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 2H4v6l4-2 4 2 4-2V5z" />
                                            </svg>
                                            <Form.Control
                                                type="file"
                                                name="image2"
                                                onChange={handleFileChange}
                                                accept="image/*"
                                                required
                                                isInvalid={!!errors.image2}
                                            />
                                            <Form.Control.Feedback type="invalid">{errors.image2}</Form.Control.Feedback>
                                        </div>
                                    </Form.Group>
                                </Col>
                                <Col md={4}>
                                    <Form.Group>
                                        <div className="d-flex align-items-center border rounded p-2">
                                            <svg
                                                className="w-5 h-5 text-muted me-2"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                            >
                                                <path d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 2H4v6l4-2 4 2 4-2V5z" />
                                            </svg>
                                            <Form.Control
                                                type="file"
                                                name="image3"
                                                onChange={handleFileChange}
                                                accept="image/*"
                                                required
                                                isInvalid={!!errors.image3}
                                            />
                                            <Form.Control.Feedback type="invalid">{errors.image3}</Form.Control.Feedback>
                                        </div>
                                    </Form.Group>
                                </Col>
                            </Row>
                        )}

                        <Col md={12}>
                            <Button
                                type="submit"
                                variant="success"
                                className="w-100 py-2 rounded"
                            >
                                Đăng ký
                            </Button>
                        </Col>
                    </Row>
                </Form>
            </div>
        </Container>
    );
};

export default Register;