package com.aashish.ecommerce_backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">

                    <title>E-Commerce Backend</title>

                    <style>

                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }

                        body {
                            min-height: 100vh;
                            font-family: Arial, Helvetica, sans-serif;
                            background:
                                radial-gradient(
                                    circle at top left,
                                    #312e81 0%,
                                    transparent 35%
                                ),
                                radial-gradient(
                                    circle at bottom right,
                                    #0f766e 0%,
                                    transparent 35%
                                ),
                                #080b16;

                            color: white;

                            display: flex;
                            align-items: center;
                            justify-content: center;

                            padding: 25px;
                        }

                        .container {
                            width: 100%;
                            max-width: 900px;

                            padding: 55px;

                            background: rgba(255, 255, 255, 0.08);

                            border: 1px solid rgba(255, 255, 255, 0.15);

                            border-radius: 28px;

                            backdrop-filter: blur(18px);
                            -webkit-backdrop-filter: blur(18px);

                            box-shadow:
                                0 30px 80px rgba(0, 0, 0, 0.45);
                        }

                        .badge {
                            display: inline-flex;

                            align-items: center;

                            gap: 8px;

                            padding: 8px 14px;

                            border-radius: 999px;

                            background: rgba(34, 197, 94, 0.12);

                            border:
                                1px solid
                                rgba(34, 197, 94, 0.35);

                            color: #86efac;

                            font-size: 14px;

                            font-weight: 600;

                            margin-bottom: 25px;
                        }

                        .dot {
                            width: 9px;
                            height: 9px;

                            background: #22c55e;

                            border-radius: 50%;

                            box-shadow:
                                0 0 14px #22c55e;
                        }

                        h1 {
                            font-size: clamp(40px, 7vw, 70px);

                            line-height: 1.05;

                            margin-bottom: 18px;

                            letter-spacing: -2px;
                        }

                        .highlight {
                            background:
                                linear-gradient(
                                    90deg,
                                    #a78bfa,
                                    #22d3ee
                                );

                            -webkit-background-clip: text;

                            -webkit-text-fill-color: transparent;
                        }

                        .subtitle {
                            color: #cbd5e1;

                            font-size: 19px;

                            line-height: 1.7;

                            max-width: 680px;

                            margin-bottom: 35px;
                        }

                        .status {
                            display: flex;

                            align-items: center;

                            gap: 12px;

                            padding: 18px 20px;

                            background:
                                rgba(15, 23, 42, 0.65);

                            border-radius: 16px;

                            border:
                                1px solid
                                rgba(255, 255, 255, 0.1);

                            margin-bottom: 35px;
                        }

                        .status strong {
                            color: #86efac;
                        }

                        .grid {
                            display: grid;

                            grid-template-columns:
                                repeat(2, 1fr);

                            gap: 16px;
                        }

                        .card {
                            padding: 22px;

                            border-radius: 18px;

                            background:
                                rgba(255, 255, 255, 0.06);

                            border:
                                1px solid
                                rgba(255, 255, 255, 0.09);

                            transition:
                                transform 0.2s ease,
                                background 0.2s ease;
                        }

                        .card:hover {
                            transform:
                                translateY(-4px);

                            background:
                                rgba(255, 255, 255, 0.10);
                        }

                        .icon {
                            font-size: 28px;

                            margin-bottom: 12px;
                        }

                        .card h3 {
                            font-size: 17px;

                            margin-bottom: 7px;
                        }

                        .card p {
                            color: #94a3b8;

                            font-size: 14px;

                            line-height: 1.5;
                        }

                        .footer {
                            margin-top: 35px;

                            padding-top: 22px;

                            border-top:
                                1px solid
                                rgba(255, 255, 255, 0.1);

                            color: #64748b;

                            font-size: 13px;

                            text-align: center;
                        }

                        @media (max-width: 650px) {

                            .container {
                                padding: 32px 22px;

                                border-radius: 22px;
                            }

                            .grid {
                                grid-template-columns: 1fr;
                            }

                            h1 {
                                letter-spacing: -1px;
                            }

                            .subtitle {
                                font-size: 16px;
                            }
                        }

                    </style>

                </head>

                <body>

                    <main class="container">

                        <div class="badge">

                            <span class="dot"></span>

                            BACKEND ONLINE

                        </div>


                        <h1>

                            E-Commerce<br>

                            <span class="highlight">
                                Backend API
                            </span>

                        </h1>


                        <p class="subtitle">

                            Your Spring Boot backend is running successfully.
                            The API is ready to receive requests and power the
                            e-commerce application.

                        </p>


                        <div class="status">

                            <span class="dot"></span>

                            <span>

                                System Status:

                                <strong>
                                    Operational
                                </strong>

                            </span>

                        </div>


                        <section class="grid">


                            <div class="card">

                                <div class="icon">
                                    ⚡
                                </div>

                                <h3>
                                    Spring Boot
                                </h3>

                                <p>
                                    RESTful backend services powered by
                                    Spring Boot.
                                </p>

                            </div>


                            <div class="card">

                                <div class="icon">
                                    🗄️
                                </div>

                                <h3>
                                    Database
                                </h3>

                                <p>
                                    MySQL database with Flyway migrations
                                    and Hibernate ORM.
                                </p>

                            </div>


                            <div class="card">

                                <div class="icon">
                                    🔐
                                </div>

                                <h3>
                                    Authentication
                                </h3>

                                <p>
                                    Secure authentication using
                                    JWT-based authorization.
                                </p>

                            </div>


                            <div class="card">

                                <div class="icon">
                                    💬
                                </div>

                                <h3>
                                    Integrations
                                </h3>

                                <p>
                                    WhatsApp Cloud API and Gemini AI
                                    integrations are configured.
                                </p>

                            </div>


                        </section>


                        <div class="footer">

                            E-Commerce Backend
                            •
                            REST API Server
                            •
                            Running on Render

                        </div>

                    </main>

                </body>

                </html>
                """;
    }
}