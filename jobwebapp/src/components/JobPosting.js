import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Api, { endpoints } from '../configs/Apis'; 
import cookie from 'react-cookies';

const PostJob = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        skills: '',
        salaryFrom: '',
        salaryTo: '',
        workingTime: '',
        location: '',
        locationLink: '',
        categoryId: ''
    });
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    // Lấy danh sách danh mục công việc khi component được mount
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const res = await Api.get(endpoints['categories']); // Giả định bạn có API để lấy danh mục
                if (Array.isArray(res.data)) {
                    setCategories(res.data);
                } else {
                    setError('Không thể tải danh sách danh mục.');
                }
            } catch (ex) {
                console.error('Lỗi khi lấy danh mục:', ex);
                setError('Không thể tải danh sách danh mục.');
            }
        };
        fetchCategories();
    }, []);

    // Xử lý thay đổi giá trị trong form
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Xử lý gửi form
    const handleSubmit = async (e) => {
        e.preventDefault();

        setError(null);
        setSuccess(null);
        setLoading(true);

        // Validate dữ liệu
        if (!formData.title || formData.title.trim() === '') {
            setError('Tiêu đề là bắt buộc.');
            setLoading(false);
            return;
        }
        if (!formData.description || formData.description.trim() === '') {
            setError('Mô tả là bắt buộc.');
            setLoading(false);
            return;
        }
        if (!formData.categoryId) {
            setError('Vui lòng chọn danh mục.');
            setLoading(false);
            return;
        }

        try {
            const jobData = {
                title: formData.title.trim(),
                description: formData.description.trim(),
                skills: formData.skills.trim(),
                salaryFrom: parseInt(formData.salaryFrom) || 0,
                salaryTo: parseInt(formData.salaryTo) || 0,
                workingTime: formData.workingTime.trim(),
                location: formData.location.trim(),
                locationLink: formData.locationLink.trim(),
                categoryId: parseInt(formData.categoryId)
            };

            const res = await Api.post(endpoints['createjob'], jobData, {
                headers: {
                    Authorization: `Bearer ${cookie.load('token')}`
                }
            });

            if (res.data.success) {
                setSuccess(res.data.message);
                setTimeout(() => {
                    navigate('/'); // Quay về trang chủ sau khi đăng thành công
                }, 1500);
            } else {
                setError(res.data.message || 'Đăng bài thất bại.');
            }
        } catch (ex) {
            console.error('Lỗi khi đăng bài:', ex);
            if (ex.response) {
                setError(ex.response.data.message || 'Lỗi máy chủ. Vui lòng thử lại.');
            } else {
                setError('Không thể kết nối đến server.');
            }
        } finally {
            setLoading(false);
        }
    };

    // Xử lý hủy (quay lại trang trước)
    const handleCancel = () => {
        navigate(-1); // Quay lại trang trước đó
    };

    return (
        <Container className="my-4">
            <h2 className="text-center mb-4">Đăng bài công việc</h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3" controlId="title">
                            <Form.Label>Tiêu đề:</Form.Label>
                            <Form.Control
                                type="text"
                                name="title"
                                value={formData.title}
                                onChange={handleChange}
                                placeholder="Nhập tiêu đề công việc"
                                required
                            />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3" controlId="categoryId">
                            <Form.Label>Danh mục:</Form.Label>
                            <Form.Select
                                name="categoryId"
                                value={formData.categoryId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Chọn danh mục</option>
                                {categories.map((category) => (
                                    <option key={category.id} value={category.id}>
                                        {category.name}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={12}>
                        <Form.Group className="mb-3" controlId="description">
                            <Form.Label>Mô tả:</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                placeholder="Nhập mô tả công việc"
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3" controlId="skills">
                            <Form.Label>Kỹ năng:</Form.Label>
                            <Form.Control
                                type="text"
                                name="skills"
                                value={formData.skills}
                                onChange={handleChange}
                                placeholder="Nhập kỹ năng yêu cầu (cách nhau bằng dấu phẩy)"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3" controlId="salaryFrom">
                            <Form.Label>Lương tối thiểu (nghìn đồng):</Form.Label>
                            <Form.Control
                                type="number"
                                name="salaryFrom"
                                value={formData.salaryFrom}
                                onChange={handleChange}
                                placeholder="Ví dụ: 25000"
                                min="0"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3" controlId="salaryTo">
                            <Form.Label>Lương tối đa (nghìn đồng):</Form.Label>
                            <Form.Control
                                type="number"
                                name="salaryTo"
                                value={formData.salaryTo}
                                onChange={handleChange}
                                placeholder="Ví dụ: 30000"
                                min="0"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3" controlId="workingTime">
                            <Form.Label>Thời gian làm việc:</Form.Label>
                            <Form.Control
                                type="text"
                                name="workingTime"
                                value={formData.workingTime}
                                onChange={handleChange}
                                placeholder="Ví dụ: Cuối tuần, Tối"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3" controlId="location">
                            <Form.Label>Địa điểm:</Form.Label>
                            <Form.Control
                                type="text"
                                name="location"
                                value={formData.location}
                                onChange={handleChange}
                                placeholder="Ví dụ: 45 Nguyễn Huệ, Q1, HCM"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={12}>
                        <Form.Group className="mb-3" controlId="locationLink">
                            <Form.Label>Link địa điểm:</Form.Label>
                            <Form.Control
                                type="text"
                                name="locationLink"
                                value={formData.locationLink}
                                onChange={handleChange}
                                placeholder="Ví dụ: https://goo.gl/maps/abc123"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <div className="d-flex justify-content-end gap-2">
                    <Button variant="primary" type="submit" disabled={loading}>
                        {loading ? 'Đang lưu...' : 'Lưu'}
                    </Button>
                    <Button variant="secondary" onClick={handleCancel} disabled={loading}>
                        Hủy
                    </Button>
                </div>
            </Form>
        </Container>
    );
};

export default PostJob;