package Singleton.server.reply;

import Singleton.server.content.controller.ContentController;
import Singleton.server.content.dto.ContentPatchDto;
import Singleton.server.content.dto.ContentPostDto;
import Singleton.server.content.dto.ContentResponseDto;
import Singleton.server.content.entity.Content;
import Singleton.server.content.mapper.ContentMapper;
import Singleton.server.content.service.ContentService;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static Singleton.server.util.ApiDocumentUtils.getRequestPreProcessor;
import static Singleton.server.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ContentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ContentControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @MockBean
    private ContentMapper contentMapper;

    @Autowired
    private Gson gson;

    @Test
    public void postContentTest() throws Exception{
        // given
        ContentPostDto post = ContentPostDto.builder()
                .memberId(1L)
                .title("??????")
                .body("??????")
                .rec(0)
                .tags("??????")
                .build();

        ContentResponseDto responseDto =
                ContentResponseDto.builder()
                        .memberId(1L)
                        .contentId(1L)
                        .title("??????")
                        .body("??????")
                        .rec(0)
                        .tags("??????")
                        .createdAt(LocalDateTime.of(2022, 10, 31, 10, 0, 0))
                        .modifiedAt(LocalDateTime.of(2022, 10, 31, 10, 0, 0))
                        .build();

        String content = gson.toJson(post);

        given(contentMapper.contentPostDtoToContent(Mockito.any(ContentPostDto.class))).willReturn(Mockito.mock(Content.class));
        given(contentService.createContent(Mockito.any(Content.class))).willReturn(Mockito.mock(Content.class));
        given(contentMapper.contentToContentResponseDto(Mockito.any(Content.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/content")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.memberId").value(post.getMemberId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.body").value(post.getBody()))
                .andExpect(jsonPath("$.data.rec").value(post.getRec()))
                .andExpect(jsonPath("$.data.tags").value(post.getTags()))
                .andDo(document("post-content",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("rec").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("tags").type(JsonFieldType.STRING).description("??????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data.contentId").type(JsonFieldType.NUMBER).description("???????????????"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.body").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.rec").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.tags").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("????????????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????????")
                                )
                        )
                ));
    }

    @Test
    public void patchContentTest() throws Exception {
        // given
        long content_Id = 1L;
        ContentPatchDto patch = ContentPatchDto.builder()
                .contentId(content_Id)
                .title("????????????")
                .body("????????????")
                .tags("??????")
                .build();

        ContentResponseDto responseDto =
                ContentResponseDto.builder()
                        .memberId(1L)
                        .contentId(1L)
                        .title("????????????")
                        .body("????????????")
                        .rec(0)
                        .tags("??????")
                        .createdAt(LocalDateTime.of(2022, 10, 31, 10, 0, 0))
                        .modifiedAt(LocalDateTime.of(2022, 10, 31, 10, 0, 0))
                        .build();

        String content = gson.toJson(patch);

        given(contentMapper.contentPatchDtoToContent(Mockito.any(ContentPatchDto.class))).willReturn(Mockito.mock(Content.class));
        given(contentService.updateContent(Mockito.any(Content.class))).willReturn(Mockito.mock(Content.class));
        given(contentMapper.contentToContentResponseDto(Mockito.any(Content.class))).willReturn(responseDto);

        // when
        ResultActions actions=
                mockMvc.perform(patch("/content/{content-id}",1,content_Id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contentId").value(patch.getContentId()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.body").value(patch.getBody()))
                .andExpect(jsonPath("$.data.tags").value(patch.getTags()))
                .andDo(document("patch-content",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("contentId").type(JsonFieldType.NUMBER).description("???????????????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????????"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("????????????"),
                                        fieldWithPath("tags").type(JsonFieldType.STRING).description("??????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data.contentId").type(JsonFieldType.NUMBER).description("???????????????"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.body").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.rec").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.tags").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("????????????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????????")
                                )
                        )
                ));
    }

    @Test
    public void getContentTest() throws Exception{
        //given

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/content/{content-id}", 1L));

        //then
        MvcResult result = actions.andExpect(status().isOk())
                .andDo(
                        document("delete-content",
                                pathParameters(
                                        parameterWithName("content-id").description("?????? ??????")
                                )
//                                ,
//                                responseFields(
//                                        fieldWithPath("data").type(JsonFieldType.STRING).description("?????? ?????? ??? ???????????????")
//                                )
                        )).andReturn();

    }

    @Test
    public void deleteContentTest() throws Exception{
        //given

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/content/{content-id}",1L));

        //then
        MvcResult result = actions.andExpect(status().isNoContent()).andDo(
                        document("delete-content",
                                pathParameters(
                                        parameterWithName("content-id").description("?????? ??????")
                                )
//                                ,
//                                responseFields(
//                                        fieldWithPath("data").type(JsonFieldType.STRING).description("?????? ?????? ??? ???????????????")
//                                )
                        )).andReturn();
    }
}
