/** date:2019-06-05 License By http://www.dogu.site */

layui.define(['jquery', 'layer'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var bodyDOM = '.layui-layout-admin>.layui-body';
    var tabDOM = bodyDOM + '>.layui-tab';
    var sideDOM = '.layui-layout-admin>.layui-side>.layui-side-scroll';
    var headerDOM = '.layui-layout-admin>.layui-header';
    var tabFilter = 'admin-pagetabs';
    var navFilter = 'admin-side-nav';
    var themeAdmin = 'theme-admin';  // 自带的主题
	
    var admin = {
        version: version,  // 版本号
        defaultTheme: 'theme-admin',  // 默认主题
        tableName: 'dogu',  // 存储表名
        // 设置侧栏折叠
        flexible: function (expand) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.flexible(expand);
                    return;
                }
            }
            var isExapnd = $('.layui-layout-admin').hasClass('admin-nav-mini');
            if (isExapnd == !expand) {
                return;
            }
            if (expand) {
                admin.hideTableScrollBar();
                $('.layui-layout-admin').removeClass('admin-nav-mini');
            } else {
                $('.layui-layout-admin').addClass('admin-nav-mini');
            }
            admin.removeNavHover();
        },
        // 设置导航栏选中
        activeNav: function (url) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.activeNav(url);
                    return;
                }
            }
            if (!url) {
                url = window.location.pathname;
                url = url.substring(url.indexOf('/'));
            }
            if (url && url != '') {
                $(sideDOM + '>.layui-nav .layui-nav-item .layui-nav-child dd').removeClass('layui-this');
                $(sideDOM + '>.layui-nav .layui-nav-item').removeClass('layui-this');
                var $a = $(sideDOM + '>.layui-nav a[lay-href="' + url + '"]');
                if ($a && $a.length > 0) {
                    if ($(sideDOM + '>.layui-nav').attr('lay-accordion') == 'true') {
                        $(sideDOM + '>.layui-nav .layui-nav-itemed').removeClass('layui-nav-itemed');
                    }
                    $a.parent().addClass('layui-this');  // 选中当前
                    $a.parent('dd').parents('.layui-nav-child').parent().addClass('layui-nav-itemed');  // 展开所有父级
                    // 适配多系统模式
                    $('ul[lay-filter="' + navFilter + '"]').addClass('layui-hide');
                    var $aUl = $a.parents('.layui-nav');
                    $aUl.removeClass('layui-hide');
                    $(headerDOM + '>.layui-nav>.layui-nav-item').removeClass('layui-this');
                    $(headerDOM + '>.layui-nav>.layui-nav-item>a[nav-bind="' + $aUl.attr('nav-id') + '"]').parent().addClass('layui-this');
                    // 菜单超出屏幕自动滚动
                    var topBeyond = $a.offset().top + $a.outerHeight() + 30 - admin.getPageHeight();
                    var topDisparity = 50 + 65 - $a.offset().top;
                    if (topBeyond > 0) {
                        $(sideDOM).animate({'scrollTop': $(sideDOM).scrollTop() + topBeyond}, 100);
                    } else if (topDisparity > 0) {
                        $(sideDOM).animate({'scrollTop': $(sideDOM).scrollTop() - topDisparity}, 100);
                    }
                } else {
                    // console.warn(url + ' is not in left side');
                }
            } else {
                console.warn('active url is null');
            }
        },
        // 右侧弹出
        popupRight: function (param) {
            if (param.title == undefined) {
                param.title = false;
                param.closeBtn = false;
            }
            if (param.anim == undefined) {
                param.anim = 2;
            }
            if (param.fixed == undefined) {
                param.fixed = true;
            }
            param.isOutAnim = false;
            param.offset = 'r';
            param.shadeClose = true;
            param.area = '336px';
            param.skin = 'layui-layer-adminRight';
            param.move = false;
            return admin.open(param);
        },
        // 封装layer.open
        open: function (param) {
            if (!param.area) {
                param.area = (param.type == 2) ? ['360px', '300px'] : '360px';
            }
            if (!param.skin) {
                param.skin = 'layui-layer-admin';
            }
            if (!param.offset) {
                param.offset = '35px';
            }
            if (param.fixed == undefined) {
                param.fixed = false;
            }
            param.resize = param.resize != undefined ? param.resize : false;
            param.shade = param.shade != undefined ? param.shade : .1;
            var eCallBack = param.end;
            param.end = function () {
                layer.closeAll('tips');
                eCallBack && eCallBack();
            };
            if (param.url) {
                (param.type == undefined) && (param.type = 1);
                var sCallBack = param.success;
                param.success = function (layero, index) {
                    admin.showLoading(layero, 2);
                    $(layero).children('.layui-layer-content').load(param.url, function () {
                        sCallBack ? sCallBack(layero, index) : '';
                        admin.removeLoading(layero, false);
                    });
                };
            }
            return layer.open(param);
        },
        // 封装ajax请求，返回数据类型为json
        req: function (url, data, success, method) {
            admin.ajax({
                url: url,
                data: data,
                type: method,
                dataType: 'json',
                success: success
            });
        },
        // 封装ajax请求
        ajax: function (param) {
            var successCallback = param.success;
            param.success = function (result, status, xhr) {
                // 判断登录过期和没有权限
                var jsonRs;
                if ('json' == param.dataType.toLowerCase()) {
                    jsonRs = result;
                } else {
                    jsonRs = admin.parseJSON(result);
                }
                jsonRs && (jsonRs = result);
                if (admin.ajaxSuccessBefore(jsonRs, param.url) == false) {
                    return;
                }
                successCallback(result, status, xhr);
            };
            param.error = function (xhr) {
                param.success({code: xhr.status, msg: xhr.statusText});
            };
            param.beforeSend = function (xhr) {
                var headers = admin.getAjaxHeaders(param.url);
                for (var i = 0; i < headers.length; i++) {
                    xhr.setRequestHeader(headers[i].name, headers[i].value);
                }
            };
            $.ajax(param);
        },
        // 判断是否为json
        parseJSON: function (str) {
            if (typeof str == 'string') {
                try {
                    var obj = JSON.parse(str);
                    if (typeof obj == 'object' && obj) {
                        return obj;
                    }
                } catch (e) {
                }
            }
        },
        // 显示加载动画
        showLoading: function (elem, type, opacity) {
            var loader = ['<div class="ball-loader"><span></span><span></span><span></span><span></span></div>', '<div class="rubik-loader"></div>'];
            if (!elem) {
                elem = 'body';
            }
            if (type == undefined) {
                type = 1;
            }
            $(elem).addClass('page-no-scroll');  // 禁用滚动条
            var $loading = $(elem).children('.page-loading');
            if ($loading.length <= 0) {
                $(elem).append('<div class="page-loading">' + loader[type - 1] + '</div>');
                $loading = $(elem).children('.page-loading');
            }
            opacity && $loading.css('background-color', 'rgba(255,255,255,' + opacity + ')');
            $loading.show();
        },
        // 移除加载动画
        removeLoading: function (elem, fade, del) {
            if (!elem) {
                elem = 'body';
            }
            if (fade == undefined) {
                fade = true;
            }
            var $loading = $(elem).children('.page-loading');
            if (del) {
                $loading.remove();
            } else {
                fade ? $loading.fadeOut() : $loading.hide();
            }
            $(elem).removeClass('page-no-scroll');
        },
        // 缓存临时数据
        putTempData: function (key, value) {
            var tableName = admin.tableName + '_tempData';
            if (value != undefined && value != null) {
                layui.sessionData(tableName, {key: key, value: value});
            } else {
                layui.sessionData(tableName, {key: key, remove: true});
            }
        },
        // 获取缓存临时数据
        getTempData: function (key) {
            var tableName = admin.tableName + '_tempData';
            var tempData = layui.sessionData(tableName);
            if (tempData) {
                return tempData[key];
            } else {
                return false;
            }
        },
        // 滑动选项卡
        rollPage: function (d) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.rollPage(d);
                    return;
                }
            }
            var $tabTitle = $(tabDOM + '>.layui-tab-title');
            var left = $tabTitle.scrollLeft();
            if ('left' === d) {
                $tabTitle.animate({'scrollLeft': left - 120}, 100);
            } else if ('auto' === d) {
                var autoLeft = 0;
                $tabTitle.children("li").each(function () {
                    if ($(this).hasClass('layui-this')) {
                        return false;
                    } else {
                        autoLeft += $(this).outerWidth();
                    }
                });
                $tabTitle.animate({'scrollLeft': autoLeft - 120}, 100);
            } else {
                $tabTitle.animate({'scrollLeft': left + 120}, 100);
            }
        },
        // 刷新当前tab
        refresh: function (url) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.refresh(url);
                    return;
                }
            }
            var $iframe;
            if (!url) {
                $iframe = $(tabDOM + '>.layui-tab-content>.layui-tab-item.layui-show>.admin-iframe');
                if (!$iframe || $iframe.length <= 0) {
                    $iframe = $(bodyDOM + '>div>.admin-iframe');
                }
            } else {
                $iframe = $(tabDOM + '>.layui-tab-content>.layui-tab-item>.admin-iframe[lay-id="' + url + '"]');
                if (!$iframe || $iframe.length <= 0) {
                    $iframe = $(bodyDOM + '>.admin-iframe');
                }
            }
            if ($iframe && $iframe[0]) {
                try {
                    $iframe[0].contentWindow.location.reload(true);
                } catch (e) {
                    $iframe.attr('src', $iframe.attr('src'));
                }
            } else {
                console.warn(url + ' is not found');
            }
        },
        // 关闭当前选项卡
        closeThisTabs: function (url) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.closeThisTabs(url);
                    return;
                }
            }
            admin.closeTabOperNav();
            var $title = $(tabDOM + '>.layui-tab-title');
            if (!url) {
                if ($title.find('li').first().hasClass('layui-this')) {
                    layer.msg('主页不能关闭', {icon: 2});
                    return;
                }
                $title.find('li.layui-this').find(".layui-tab-close").trigger("click");
            } else {
                if (url == $title.find('li').first().attr('lay-id')) {
                    layer.msg('主页不能关闭', {icon: 2});
                    return;
                }
                $title.find('li[lay-id="' + url + '"]').find(".layui-tab-close").trigger("click");
            }
        },
        // 关闭其他选项卡
        closeOtherTabs: function (url) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.closeOtherTabs(url);
                    return;
                }
            }
            if (!url) {
                $(tabDOM + '>.layui-tab-title li:gt(0):not(.layui-this)').find('.layui-tab-close').trigger('click');
            } else {
                $(tabDOM + '>.layui-tab-title li:gt(0)').each(function () {
                    if (url != $(this).attr('lay-id')) {
                        $(this).find('.layui-tab-close').trigger('click');
                    }
                });
            }
            admin.closeTabOperNav();
        },
        // 关闭所有选项卡
        closeAllTabs: function () {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.closeAllTabs();
                    return;
                }
            }
            $(tabDOM + '>.layui-tab-title li:gt(0)').find('.layui-tab-close').trigger('click');
            $(tabDOM + '>.layui-tab-title li:eq(0)').trigger('click');
            admin.closeTabOperNav();
        },
        // 关闭选项卡操作菜单
        closeTabOperNav: function () {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    top.layui.admin.closeTabOperNav();
                    return;
                }
            }
            $('.layui-icon-down .layui-nav .layui-nav-child').removeClass('layui-show');
        },
        // 设置主题
        changeTheme: function (theme) {
            if (theme) {
                layui.data(admin.tableName, {key: 'theme', value: theme});
                if (themeAdmin == theme) {
                    theme = undefined;
                }
            } else {
                layui.data(admin.tableName, {key: 'theme', remove: true});
            }
            admin.removeTheme(top);
            (theme && top.layui) && top.layui.link(admin.getThemeDir() + theme + admin.getCssSuffix(), theme);
            var ifs = top.window.frames;
            for (var i = 0; i < ifs.length; i++) {
                var tif = ifs[i];
                admin.removeTheme(tif);
                if (theme && tif.layui) {
                    tif.layui.link(admin.getThemeDir() + theme + admin.getCssSuffix(), theme);
                }
                // iframe下还有iframe的情况
                var sifs = tif.frames;
                for (var j = 0; j < sifs.length; j++) {
                    var stif = sifs[j];
                    admin.removeTheme(stif);
                    if (theme && stif.layui) {
                        stif.layui.link(admin.getThemeDir() + theme + admin.getCssSuffix(), theme);
                    }
                }
            }
        },
        // 移除主题
        removeTheme: function (w) {
            if (!w) {
                w = window;
            }
            if (w.layui) {
                var themeId = 'layuicss-theme';
                w.layui.jquery('link[id^="' + themeId + '"]').remove();
            }
        },
        // 获取主题目录
        getThemeDir: function () {
            return layui.cache.base + 'theme/';
        },
        // 关闭iframe所在的layer弹窗
        closeThisDialog: function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        },
        // 关闭所在的弹窗
        closeDialog: function (elem) {
            var id = $(elem).parents('.layui-layer').attr('id').substring(11);
            layer.close(id);
        },
        // 让当前的ifram弹层自适应高度
        iframeAuto: function () {
            parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));
        },
        // 获取浏览器高度
        getPageHeight: function () {
            return document.documentElement.clientHeight || document.body.clientHeight;
        },
        // 获取浏览器宽度
        getPageWidth: function () {
            return document.documentElement.clientWidth || document.body.clientWidth;
        },
        // 关闭导航菜单折叠悬浮效果
        removeNavHover: function () {
            $('.admin-nav-hover>.layui-nav-child').css({
                'top': 'auto',
                'max-height': 'none',
                'overflow': 'auto'
            });
            $('.admin-nav-hover').removeClass('admin-nav-hover');
        },
        // 自动计算导航菜单悬浮的样式
        setNavHoverCss: function ($that) {
            var $nav = $('.admin-nav-hover>.layui-nav-child');
            if ($that && $nav.length > 0 && $that.offset()) {
                var isOver = ($that.offset().top + $nav.outerHeight()) > window.innerHeight;  // 是否溢出屏幕
                if (isOver) {
                    var newTop = $that.offset().top - $nav.outerHeight() + $that.outerHeight();
                    if (newTop < 50) {
                        var pageHeight = admin.getPageHeight();
                        if ($that.offset().top < pageHeight / 2) {
                            $nav.css({
                                'top': '50px',
                                'max-height': pageHeight - 50 + 'px',
                                'overflow': 'auto'
                            });
                        } else {
                            $nav.css({
                                'top': $that.offset().top,
                                'max-height': pageHeight - $that.offset().top,
                                'overflow': 'auto'
                            });
                        }
                    } else {
                        $nav.css('top', newTop);
                    }
                } else {
                    $nav.css('top', $that.offset().top);
                }
                isHover = true;
            }
        },
        // 获取主题文件后缀
        getCssSuffix: function () {
            var cssSuffix = '.css';
            if (admin.version != undefined) {
                cssSuffix += '?v=';
                if (admin.version == true) {
                    cssSuffix += new Date().getTime();
                } else {
                    cssSuffix += admin.version;
                }
            }
            return cssSuffix;
        },
        // 解决窗口缩放表格滚动条闪现
        hideTableScrollBar: function (win) {
            if (admin.getPageWidth() > 750) {
                if (!win) {
                    var $iframe = $(tabDOM + '>.layui-tab-content>.layui-tab-item.layui-show>.admin-iframe');
                    if ($iframe.length <= 0) {
                        $iframe = $(bodyDOM + '>div>.admin-iframe');
                    }
                    if ($iframe.length > 0) {
                        win = $iframe[0].contentWindow;
                    }
                }
                if (win && win.layui && win.layui.jquery) {
                    if (window.hsbTimer) {
                        clearTimeout(hsbTimer);
                    }
                    win.layui.jquery('.layui-table-body.layui-table-main').addClass('no-scrollbar');
                    window.hsbTimer = setTimeout(function () {
                        if (win && win.layui && win.layui.jquery) {
                            win.layui.jquery('.layui-table-body.layui-table-main').removeClass('no-scrollbar');
                        }
                    }, 500);
                }
            }
        },
        // 绑定表单弹窗
        modelForm: function (layero, btnFilter, formFilter) {
            var $layero = $(layero);
            $layero.addClass('layui-form');
            if (formFilter) {
                $layero.attr('id', formFilter);
                $layero.attr('lay-filter', formFilter);
            }
            // 确定按钮绑定submit
            var $btnSubmit = $layero.find('.layui-layer-btn .layui-layer-btn0');
            $btnSubmit.attr('lay-submit', '');
            $btnSubmit.attr('lay-filter', btnFilter);
            // 移除多余元素
            var $content = $layero.children('.layui-layer-content');
            $content.find('[ew-event="closePageDialog"]').remove();
            $content.find('[lay-submit]').remove();
        },
        // loading按钮
        btnLoading: function (elem, text, loading) {
            if (text != undefined && (typeof text == "boolean")) {
                loading = text;
                text = undefined;
            }
            (loading == undefined) && (loading = true);
            var $elem = $(elem);
            if (loading) {
                text && $elem.html(text);
                $elem.find('.layui-icon').addClass('layui-hide');
                $elem.addClass('icon-btn');
                $elem.prepend('<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop ew-btn-loading"></i>');
                $elem.prop('disabled', 'disabled');
            } else {
                $elem.find('.ew-btn-loading').remove();
                $elem.removeProp('disabled', 'disabled');
                if ($elem.find('.layui-icon.layui-hide').length <= 0) {
                    $elem.removeClass('icon-btn');
                }
                $elem.find('.layui-icon').removeClass('layui-hide');
                text && $elem.html(text);
            }
        },
        // 鼠标移入侧边栏自动展开
        openSideAutoExpand: function () {
            $('.layui-layout-admin>.layui-side').off('mouseenter.openSideAutoExpand').on("mouseenter.openSideAutoExpand", function () {
                if ($(this).parent().hasClass('admin-nav-mini')) {
                    admin.flexible(true);
                    $(this).addClass('side-mini-hover');
                }
            });
            $('.layui-layout-admin>.layui-side').off('mouseleave.openSideAutoExpand').on("mouseleave.openSideAutoExpand", function () {
                if ($(this).hasClass('side-mini-hover')) {
                    admin.flexible(false);
                    $(this).removeClass('side-mini-hover');
                }
            });
        },
        // 表格单元格超出内容自动展开
        openCellAutoExpand: function () {
            $('body').off('mouseenter.openCellAutoExpand').on('mouseenter.openCellAutoExpand', '.layui-table-view td', function () {
                $(this).find('.layui-table-grid-down').trigger('click');
            });
            $('body').off('mouseleave.openCellAutoExpand').on('mouseleave.openCellAutoExpand', '.layui-table-tips>.layui-layer-content', function () {
                $('.layui-table-tips-c').trigger('click');
            });
        },
        // 判断是否是index页
        isTop: function () {
            return $(bodyDOM).length > 0;
        },
        // ajax预处理
        ajaxSuccessBefore: function (res, requestUrl) {
            return true;
        },
        // ajax统一传递header
        getAjaxHeaders: function (requestUrl) {
            var headers = new Array();
            return headers;
        }
    };

    // admin提供的事件
    admin.events = {
        // 折叠侧导航
        flexible: function (e) {
            var expand = $('.layui-layout-admin').hasClass('admin-nav-mini');
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.jquery) {
                    expand = top.layui.jquery('.layui-layout-admin').hasClass('admin-nav-mini');
                }
            }
            admin.flexible(expand);
        },
        // 刷新主体部分
        refresh: function () {
            admin.refresh();
        },
        // 后退
        back: function () {
            history.back();
        },
        // 设置主题
        theme: function () {
            var ta;
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    ta = top.layui.admin;
                } else {
                    ta = admin;
                }
            } else {
                ta = admin;
            }
            var url = $(this).attr('data-url');
            ta.popupRight({
                id: 'layer-theme',
                type: 2,
                content: url ? url : rootPath+'/main/changeUI'
            });
        },
        // 打开便签
        note: function () {
            var ta;
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    ta = top.layui.admin;
                } else {
                    ta = admin;
                }
            } else {
                ta = admin;
            }
            var url = $(this).attr('data-url');
            ta.popupRight({
                id: 'layer-note',
                title: '便签',
                type: 2,
                closeBtn: false,
                content: url ? url : rootPath+'/main/note'
            });
        },
        // 打开消息
        message: function () {
            var ta;
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.admin) {
                    ta = top.layui.admin;
                } else {
                    ta = admin;
                }
            } else {
                ta = admin;
            }
            var url = $(this).attr('data-url');
            ta.popupRight({
                id: 'layer-notice',
                type: 2,
                content: url ? url : rootPath+'/main/message'
            });
        },
        info: function () {
            getUserInfo();
        },
        // 打开修改密码弹窗
        psw: function () {
            ChangePsw();
        },
        lock: function (){
        	prepareLock();
        },
        // 退出登录
        logout: function () {
            var url = $(this).attr('data-url');
            layer.confirm('确定要退出登录吗？', {
                title: '温馨提示',
                skin: 'layui-layer-admin'
            }, function () {
                $.ajax({
        			type : 'post',
        			url : rootPath + '/loginout',
        			success : function(data) {
        				if (data == "success") {
        					window.location.href = rootPath;
        				}
        			},
        			error : function(XMLHttpRequest, textStatus, errorThrown) {
        				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
        					 || XMLHttpRequest.status == "404"){
        					parent.layer.alert(ForbiddenMsg,{icon: 7});
        				}else{
        					parent.layer.alert(errorThrown,{icon: 7});
        				}
        			}
        		});
            });
        },
        // 全屏
        fullScreen: function (e) {
            var ac = 'layui-icon-screen-full', ic = 'layui-icon-screen-restore';
            var ti = $(this).find('i');

            var isFullscreen = document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || false;
            if (isFullscreen) {
                var efs = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
                if (efs) {
                    efs.call(document);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }
                ti.addClass(ac).removeClass(ic);
            } else {
                var el = document.documentElement;
                var rfs = el.requestFullscreen || el.webkitRequestFullscreen || el.mozRequestFullScreen || el.msRequestFullscreen;
                if (rfs) {
                    rfs.call(el);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }
                ti.addClass(ic).removeClass(ac);
            }
        },
        // 左滑动tab
        leftPage: function () {
            admin.rollPage("left");
        },
        // 右滑动tab
        rightPage: function () {
            admin.rollPage();
        },
        // 关闭当前选项卡
        closeThisTabs: function () {
            admin.closeThisTabs();
        },
        // 关闭其他选项卡
        closeOtherTabs: function () {
            admin.closeOtherTabs();
        },
        // 关闭所有选项卡
        closeAllTabs: function () {
            admin.closeAllTabs();
        },
        // 关闭当前iframe弹窗
        closeDialog: function () {
            admin.closeThisDialog();
        },
        // 关闭当前页面弹窗
        closePageDialog: function () {
            admin.closeDialog(this);
        }
    };

    // 所有ew-event
    $(document).on('click', '*[ew-event]', function () {
        var event = $(this).attr('ew-event');
        var te = admin.events[event];
        te && te.call(this, $(this));
    });

    // 所有lay-tips处理
    $(document).on('mouseenter', '*[lay-tips]', function () {
        var tipText = $(this).attr('lay-tips');
        var dt = $(this).attr('lay-direction');
        var bgColor = $(this).attr('lay-bg');
        layer.tips(tipText, this, {tips: [dt || 3, bgColor || '#333333'], time: -1});
    }).on('mouseleave', '*[lay-tips]', function () {
        layer.closeAll('tips');
    });

    // 侧导航折叠状态下鼠标经过显示提示
    var isHover = false;
    $(document).on('mouseenter', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a', function () {
        if (admin.getPageWidth() > 750) {
            var $that = $(this);
            $('.admin-nav-hover>.layui-nav-child').css('top', 'auto');
            $('.admin-nav-hover').removeClass('admin-nav-hover');
            $that.parent().addClass('admin-nav-hover');
            var $nav = $('.admin-nav-hover>.layui-nav-child');
            if ($nav.length > 0) {
                admin.setNavHoverCss($that);
            } else {
                var tipText = $that.find('cite').text();
                layer.tips(tipText, $that, {tips: [2, '#333333'], time: -1});
            }
        }
    }).on('mouseleave', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a', function () {
        layer.closeAll('tips');
    });

    // 鼠标离开侧导航关闭折叠浮窗
    $(document).on('mouseleave', '.layui-layout-admin.admin-nav-mini .layui-side', function () {
        isHover = false;
        setTimeout(function () {
            if (!isHover) {
                admin.removeNavHover();
            }
        }, 500);
    });

    $(document).on('mouseenter', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item.admin-nav-hover .layui-nav-child', function () {
        isHover = true;
    });

    // 所有ew-href处理
    $(document).on('click', '*[ew-href]', function () {
        var url = $(this).attr('ew-href');
        var title = $(this).attr('ew-title');
        title || (title = $(this).text());
        if (top.layui && top.layui.index) {
            top.layui.index.openTab({
                title: title ? title : '',
                url: url
            });
        } else {
            location.href = title;
        }
    });

    // 加载缓存的主题
    var cacheSetting = layui.data(admin.tableName);
    if (cacheSetting && cacheSetting.theme) {
        (cacheSetting.theme == themeAdmin) || layui.link(admin.getThemeDir() + cacheSetting.theme + admin.getCssSuffix(), cacheSetting.theme);
    } else if (themeAdmin != admin.defaultTheme) {
        layui.link(admin.getThemeDir() + admin.defaultTheme + admin.getCssSuffix(), admin.defaultTheme);
    }

    // 帮助鼠标右键菜单完成点击空白关闭的功能
    if (!layui.contextMenu) {
        $(document).off('click.ctxMenu').on('click.ctxMenu', function () {
            var ifs = top.window.frames;
            for (var i = 0; i < ifs.length; i++) {
                var tif = ifs[i];
                (tif.layui && tif.layui.jquery) && tif.layui.jquery('body>.ctxMenu').remove();
            }
            (top.layui && top.layui.jquery) && top.layui.jquery('body>.ctxMenu').remove();
        });
    }
    exports('admin', admin);
});