package com.server.wordwaves.service.implement;

import org.springframework.stereotype.Service;

import com.server.wordwaves.service.WordService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordServiceImp implements WordService {}
