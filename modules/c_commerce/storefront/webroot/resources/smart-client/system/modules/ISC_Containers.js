/*
 * Isomorphic SmartClient
 * Version v8.2p_2012-06-03 (2012-06-03)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */

if (window.isc && window.isc.module_Core && !window.isc.module_Containers) {
    isc.module_Containers = 1;
    isc._moduleStart = isc._Containers_start = (isc.timestamp ? isc.timestamp() : new Date().getTime());
    if (isc._moduleEnd && (!isc.Log || (isc.Log && isc.Log.logIsDebugEnabled('loadTime')))) {
        isc._pTM = {
            message: 'Containers load/parse time: ' + (isc._moduleStart - isc._moduleEnd) + 'ms',
            category: 'loadTime'
        };
        if (isc.Log && isc.Log.logDebug) isc.Log.logDebug(isc._pTM.message, 'loadTime');
        else if (isc._preLog) isc._preLog[isc._preLog.length] = isc._pTM;
        else isc._preLog = [isc._pTM]
    }
    isc.definingFramework = true;
    isc.ClassFactory.defineClass("ImgTab", "StretchImgButton");
    isc.A = isc.ImgTab.getPrototype();
    isc.A.capSize = 2;
    isc.A.skinImgDir = "images/Tab/";
    isc.A.labelSkinImgDir = "images/";
    isc.A.baseStyle = "tab";
    isc.A.src = "[SKIN]tab.gif";
    isc.A.showRollOver = false;
    isc.A.showFocused = true;
    isc.A.actionType = isc.Button.RADIO;
    isc.A.mozOutlineOffset = "0px";
    isc.A = isc.ImgTab.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.setPane = function isc_ImgTab_setPane(_1) {
            this.parentElement.parentElement.updateTab(this, _1)
        }
        , isc.A.selectTab = function isc_ImgTab_selectTab() {
            this.parentElement.parentElement.selectTab(this)
        }
        , isc.A.initWidget = function isc_ImgTab_initWidget(_1, _2, _3, _4, _5, _6) {
            if (this.vertical && this.titleStyle) this.titleStyle = "v" + this.titleStyle;
            return this.invokeSuper(isc.ImgTab, this.$oc, _1, _2, _3, _4, _5, _6)
        }
    );
    isc.B._maxIndex = isc.C + 3;
    isc.ClassFactory.defineClass("TabBar", "Toolbar");
    isc.A = isc.TabBar.getPrototype();
    isc.A.breadth = 21;
    isc.A.buttonConstructor = isc.ImgTab;
    isc.A.tabWithinToolbar = false;
    isc.A.skinImgDir = "images/Tab/";
    isc.A.moreTabCount = 5;
    isc.A.baseLineThickness = 1;
    isc.A.baseLineSrc = "[SKIN]baseline.gif";
    isc.A.baseLineCapSize = 2;
    isc.A.tabBarPosition = isc.Canvas.TOP;
    isc.A.selectedTab = 0;
    isc.A.defaultTabSize = 80;
    isc.A = isc.TabBar.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.initWidget = function isc_TabBar_initWidget() {
            for (var i = 0; i < this.tabs.length; i++) {
                var _2 = this.tabs[i].pane;
                if (isc.isA.String(_2) && isc.isA.Canvas(window[_2])) {
                    this.tabs[i].pane = window[_2]
                }
            }
            this.buttons = this.tabs;
            if (this.moreTab) {
                this.$79t = this.buttons.length;
                this.buttons[this.$79t] = this.moreTab
            }
            this.skinImgDir = this.skinImgDir + this.tabBarPosition + "/";
            var _3 = this.tabDefaults;
            if (_3 == null) _3 = this.tabDefaults = {};
            if (isc.Browser.isSafari) _3.showFocusOutline = false;
            _3 = this.buttonDefaults = isc.addProperties({}, this.buttonDefaults, _3);
            _3.actionType = isc.StatefulCanvas.RADIO;
            if (this.vertical) {
                _3.defaultHeight = this.defaultTabSize
            } else {
                _3.defaultWidth = this.defaultTabSize
            }
            _3.overflow = isc.Canvas.VISIBLE;
            _3.vertical = (this.tabBarPosition == isc.Canvas.LEFT || this.tabBarPosition == isc.Canvas.RIGHT);
            var _4 = isc.ClassFactory.getClass(this.buttonConstructor);
            if (_4 && _4.isA("ImgTab")) {
                _3.skinImgDir = _4.getInstanceProperty("skinImgDir") + this.tabBarPosition + "/"
            }
            _3.iconMouseDown = this.$42k;
            _3.handleDoubleClick = function () {
                var _5 = this.parentElement.parentElement;
                if (_5 && _5.titleEditEvent == "doubleClick" && _5.$798(this)) return;
                return this.Super("handleDoubleClick", arguments)
            };
            _3.handleClick = function () {
                var _5 = this.parentElement.parentElement;
                if (_5 && _5.titleEditEvent == "click" && _5.$798(this)) return;
                return this.Super("handleClick", arguments)
            }, _3._generated = true;
            var _6 = this.tabBarPosition + "StyleName";
            if (this[_6]) this.setStyleName(this[_6]);
            this.Super(this.$oc);
            if (this._baseLine == null) this.makeBaseLine()
        }
        , isc.A.isShowingMoreTab = function isc_TabBar_isShowingMoreTab() {
            return (this.showMoreTab && this.moreTab && this.$79t >= 0 && this.getMembers(this.$79t).isVisible && this.getMembers(this.$79t).isVisible())
        }
        , isc.A.$42k = function isc_TabBar__tabIconClickHandler() {
            return this.parentElement.tabIconClick(this)
        }
        , isc.A.tabIconClick = function isc_TabBar_tabIconClick(_1) {
            var _2 = this.parentElement;
            return _2.$52a(_1)
        }
        , isc.A.setButtons = function isc_TabBar_setButtons(_1) {
            this.Super("setButtons", arguments);
            if (isc.Browser.isSGWT) {
                var _2 = this.getMembers();
                for (var i = 0; i < _2.length; i++) {
                    _2[i].__ref = null
                }
            }
            if (this.showMoreTab && this.buttons.length - 1 > this.moreTabCount) {
                for (var i = this.moreTabCount - 1; i < this.buttons.length; i++) {
                    this.getMember(i).hide()
                }
                this.getMember(this.$79t).show()
            } else if (this.showMoreTab && this.moreTab) {
                this.getMember(this.$79t).hide()
            }
        }
        , isc.A.makeButton = function isc_TabBar_makeButton(_1, _2, _3, _4, _5) {
            var _6 = this.parentElement.canCloseTab(_1);
            isc.addProperties(_1, this.getCloseIconProperties(_1, _6));
            _1.locatorParent = this.parentElement;
            return this.invokeSuper("TabBar", "makeButton", _1, _2, _3, _4, _5)
        }
        , isc.A.getCloseIconProperties = function isc_TabBar_getCloseIconProperties(_1, _2) {
            var _3 = {};
            if (_1.canClose == true || (_1.canClose == null && _2)) {
                _3.icon = (_1.closeIcon || this.parentElement.closeTabIcon);
                _3.iconSize = (_1.closeIconSize || this.parentElement.closeTabIconSize);
                _3.iconOrientation = isc.Page.isRTL() ? "left" : "right";
                _3.iconAlign = _3.iconOrientation
            } else {
                _3.icon = (_1.icon);
                _3.iconSize = (_1.iconSize);
                _3.iconOrientation = _1.iconOrientation;
                _3.iconAlign = _1.iconAlign
            }
            return _3
        }
        , isc.A.addTabs = function isc_TabBar_addTabs(_1, _2) {
            if (!_2 && this.tabBarPosition == isc.Canvas.LEFT) _2 = 0;
            this.addButtons(_1, _2);
            if (isc.Browser.isSGWT) {
                var _3 = this.getMembers();
                for (var i = 0; i < _3.length; i++) {
                    _3[i].__ref = null
                }
            }
            if (this.showMoreTab && this.moreTab) {
                var _5 = this.getMembers();
                if (_5.length - 1 > this.moreTabCount) {
                    for (var i = this.moreTabCount - 1; i < _5.length; i++) {
                        _5[i].hide()
                    }
                    this.$79t = _5.length - 1;
                    _5[this.$79t].show()
                }
            }
            if (this._baseLine != null) {
                this._baseLine.bringToFront();
                var _6 = this.getButton(this.getSelectedTab());
                if (_6) _6.bringToFront()
            }
        }
        , isc.A.removeTabs = function isc_TabBar_removeTabs(_1) {
            if (_1 == null) return;
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _2 = this.map("getButton", _1);
            this.removeButtons(_1);
            if (this.showMoreTab && this.moreTab && this.$79t > 0) {
                var _3 = this.getMembers();
                for (var i = 0; i < _3.length; i++) {
                    if (i < this.moreTabCount) _3[i].show(); else _3[i].hide()
                }
                if (_3.length - 1 <= this.moreTabCount) {
                    this.$79t = null;
                    _3[_3.length - 1].hide()
                } else {
                    this.$79t = _3.length - 1
                }
            }
            for (var i = 0; i < _2.length; i++) {
                if (_2[i] != null) _2[i].destroy()
            }
        }
        , isc.A.draw = function isc_TabBar_draw(_1, _2, _3, _4) {
            arguments.$cw = this;
            this.fixLayout();
            this.invokeSuper(isc.TabBar, "draw", _1, _2, _3, _4);
            this.bringToFront();
            var _5 = this.getButton(this.selectedTab);
            if (_5) {
                _5.setSelected(true)
            }
        }
        , isc.A.makeBaseLine = function isc_TabBar_makeBaseLine() {
            this._baseLine = this.addAutoChild("baseLine", {
                ID: this.getID() + "_baseLine",
                vertical: (this.tabBarPosition == isc.Canvas.LEFT || this.tabBarPosition == isc.Canvas.RIGHT),
                skinImgDir: this.skinImgDir,
                src: this.baseLineSrc,
                capSize: this.baseLineCapSize,
                imageType: isc.Img.STRETCH,
                overflow: "hidden",
                addAsChild: true,
                autoDraw: false
            }, isc.StretchImg);
            this.ignoreMemberZIndex(this._baseline)
        }
        , isc.A.scrollTo = function isc_TabBar_scrollTo(_1, _2, _3, _4, _5, _6) {
            this.invokeSuper(isc.TabBar, "scrollTo", _1, _2, _3, _4, _5, _6);
            if (this._baseLine) this.fixLayout()
        }
        , isc.A.fixLayout = function isc_TabBar_fixLayout() {
            var _1 = this._baseLine;
            if (_1 == null) return;
            var _2 = this.parentElement, _3 = 0;
            if (this.tabBarPosition == isc.Canvas.TOP) {
                _1.setRect(this.getScrollLeft(), this.getHeight() - this.baseLineThickness, this.parentElement.getWidth() - _3, this.baseLineThickness)
            } else if (this.tabBarPosition == isc.Canvas.BOTTOM) {
                _1.setRect(this.getScrollLeft(), 0, this.parentElement.getWidth() - _3, this.baseLineThickness)
            } else if (this.tabBarPosition == isc.Canvas.LEFT) {
                _1.setRect(this.getWidth() - this.baseLineThickness, this.getScrollTop(), this.baseLineThickness, this.parentElement.getHeight() - _3)
            } else if (this.tabBarPosition == isc.Canvas.RIGHT) {
                _1.setRect(0, this.getScrollTop(), this.baseLineThickness, this.parentElement.getHeight() - _3)
            }
        }
        , isc.A.layoutChildren = function isc_TabBar_layoutChildren(_1, _2, _3, _4) {
            this.invokeSuper(isc.TabBar, "layoutChildren", _1, _2, _3, _4);
            this.fixLayout()
        }
        , isc.A.buttonSelected = function isc_TabBar_buttonSelected(_1) {
            this.ignoreMemberZIndex(_1);
            _1.bringToFront();
            this.lastSelectedButton = _1;
            this.$6b(this.lastSelectedButton)
        }
        , isc.A.buttonDeselected = function isc_TabBar_buttonDeselected(_1) {
            _1.sendToBack();
            this.stopIgnoringMemberZIndex(_1)
        }
        , isc.A.getSelectedTab = function isc_TabBar_getSelectedTab() {
            return this.getButtonNumber(this.getSelectedButton())
        }
        , isc.A.selectTab = function isc_TabBar_selectTab(_1) {
            this.selectedTab = _1;
            this.selectButton(_1)
        }
        , isc.A.setupButtonFocusProperties = function isc_TabBar_setupButtonFocusProperties() {
            this.$6b(this.getButton(this.selectedTab));
            return this.Super("setupButtonFocusProperties", arguments)
        }
        , isc.A.$6b = function isc_TabBar__updateFocusButton(_1) {
            if (!this.selectTabOnContextClick && isc.EH.rightButtonDown()) {
                if (this.$6g != null && this.getButton(_1) != this.$6g) {
                    var _2 = this.getButtonNumber(this.$6g);
                    this.$6g.focus();
                    var _3 = this;
                    isc.Timer.setTimeout(function () {
                        var _4 = _3.getButton(_2);
                        if (!_4) return;
                        if (!isc.EH.targetIsMasked(_4)) {
                            _4.focus()
                        } else {
                            _3.selectTab(_2)
                        }
                    }, 0)
                }
                return
            }
            var _5 = this.getButton(_1);
            if (_5) _5.focus();
            this.Super("$6b", arguments);
            if (this.$6g != null && !this.$6g.selected) {
                this.selectTab(this.$6g)
            }
        }
        , isc.A.$7b = function isc_TabBar__scrollForward(_1, _2) {
            if (this.overflow == isc.Canvas.VISIBLE || !this.members || this.members.length == 0) return;
            var _3, _4;
            if (this.$7c != null) {
                _3 = this.members[this.$7c + (_1 ? -1 : 1)];
                if (_3 == null) {
                    return
                }
                _4 = (_1 ? (this.vertical ? _3.getTop() : _3.getLeft()) : (this.vertical ? _3.getBottom() : _3.getRight()))
            } else {
                var _5 = (this.vertical ? this.getScrollHeight() : this.getScrollWidth());
                if (_5 <= (this.vertical ? this.getViewportHeight() : this.getViewportWidth()))
                    return;
                var _6 = (this.vertical ? this.getScrollTop() : this.getScrollLeft()),
                    _7 = (this.vertical ? this.getViewportHeight() : this.getViewportWidth());
                var _8 = 5;
                for (var i = 0; i < this.members.length; i++) {
                    _3 = (_1 ? this.members[this.members.length - (i + 1)] : this.members[i]);
                    _4 = (_1 ? (this.vertical ? _3.getTop() : _3.getLeft()) : (this.vertical ? _3.getBottom() : _3.getRight()));
                    var _10 = _1 ? (_4 + _8 < _6) : (_4 - _8 > (_6 + _7));
                    if (_10) break
                }
            }
            if (_2) {
                this.$7c = this.members.indexOf(_3);
                this.scrollTabIntoView(_3, _1, true, "this.$7d(" + this.$7c + ")")
            } else this.scrollTabIntoView(_3, _1)
        }
        , isc.A.$7d = function isc_TabBar__completeScroll(_1) {
            if (this.$7c == _1) delete this.$7c
        }
        , isc.A.scrollTabIntoView = function isc_TabBar_scrollTabIntoView(_1, _2, _3, _4) {
            var _5;
            if (isc.isA.Number(_1)) {
                _5 = _1;
                _1 = this.members[_1]
            } else {
                _5 = this.members.indexOf(_1)
            }
            if (!_1) return;
            if (this.$3n || this.$2z) {
                this.$67y = [_1, _2, _3, _4];
                return
            }
            var _6 = _1.getRect(), _7, _8;
            var _9 = this.vertical;
            if (_2 == null) {
                if (_5 == 0) _2 = true; else if (_5 == (this.members.getLength() - 1)) _2 = false; else {
                    if (_9) {
                        if (this.getScrollTop() > _6[1]) _2 = true; else _2 = false
                    } else {
                        if (this.getScrollLeft() > _6[0]) _2 = true; else _2 = false
                    }
                }
            }
            if (_9) {
                _8 = (_2 ? "top" : "bottom");
                _7 = "left";
                _6[2] = 0
            } else {
                _7 = (_2 ? "left" : "right");
                _8 = "top";
                _6[3] = 0
            }
            if (_5 == 0) _6[0] = _6[1] = 0;
            this.scrollIntoView(_6[0], _6[1], _6[2], _6[3], _7, _8, _3, {
                target: this,
                methodName: "scrolledTabIntoView",
                args: [_1, _4]
            })
        }
        , isc.A.scrolledTabIntoView = function isc_TabBar_scrolledTabIntoView(_1, _2) {
            if (_2 != null) {
                this.fireCallback(_2, "tab", [_1])
            }
        }
        , isc.A.$3j = function isc_TabBar__layoutChildrenDone(_1, _2, _3, _4, _5) {
            this.invokeSuper(isc.TabBar, "$3j", _1, _2, _3, _4, _5);
            if (this.$67y != null) {
                var _6 = this.$67y;
                this.scrollTabIntoView(_6[0], _6[1], _6[2], _6[3]);
                delete this.$67y
            }
        }
        , isc.A.scrollForward = function isc_TabBar_scrollForward(_1) {
            this.$7b(false, _1)
        }
        , isc.A.scrollBack = function isc_TabBar_scrollBack(_1) {
            this.$7b(true, _1)
        }
    );
    isc.B._maxIndex = isc.C + 27;
    isc.ClassFactory.defineClass("Window", "Layout");
    isc.A = isc.Window.getPrototype();
    isc.A.styleName = "windowBackground";
    isc.A.skinImgDir = "images/Window/";
    isc.A.backgroundColor = "#DDDDDD";
    isc.A.layoutMargin = 2;
    isc.A.membersMargin = 2;
    isc.A.orientation = "vertical";
    isc.A.dragStartDistance = 1;
    isc.A.canDragReposition = true;
    isc.A.dragAppearance = isc.EventHandler.OUTLINE;
    isc.A.canDragResize = false;
    isc.A.resizeFrom = ["R", "B", "BR"];
    isc.A.minWidth = 100;
    isc.A.minHeight = 100;
    isc.A.useBackMask = isc.Browser.isIE && isc.Browser.minorVersion >= 5.5 && isc.Browser.version < 9;
    isc.A.isModal = false;
    isc.A.modalMaskOpacity = 50;
    isc.A.modalMaskStyle = "modalMask";
    isc.A.modalMaskConstructor = "ScreenSpan";
    isc.A.dismissOnOutsideClick = false;
    isc.A.showBody = true;
    isc.A.bodyStyle = "windowBody";
    isc.A.bodyColor = "#FFFFFF";
    isc.A.hiliteBodyColor = "#EEEEEE";
    isc.A.contentsType = "page";
    isc.A.bodyDefaults = {layoutMargin: 0};
    isc.A.contentLayout = "vertical";
    isc.A.autoSize = false;
    isc.A.showHeader = true;
    isc.A.headerConstructor = "HLayout";
    isc.A.showHeaderBackground = !(isc.Browser.isIE && !isc.Browser.isStrict && isc.Browser.minorVersion >= 5.5);
    isc.A.headerBackgroundConstructor = "Img";
    isc.A.headerBackgroundDefaults = {width: "100%", height: "100%", addAsChild: true, vertical: false, capSize: 10};
    isc.A.headerStyle = "windowHeader";
    isc.A.headerSrc = (!(isc.Browser.isIE && !isc.Browser.isStrict && isc.Browser.minorVersion >= 5.5) ? "[SKIN]Window/headerGradient.gif" : null);
    isc.A.headerDefaults = {height: 18, layoutMargin: 1, membersMargin: 2, overflow: isc.Canvas.HIDDEN};
    isc.A.headerControls = ["headerIcon", "headerLabel", "minimizeButton", "maximizeButton", "closeButton"];
    isc.A.hiliteHeaderStyle = "windowHeaderHilite";
    isc.A.hiliteHeaderSrc = (!(isc.Browser.isIE && isc.Browser.minorVersion >= 5.5) ? "[SKIN]Window/headerGradient_hilite.gif" : null);
    isc.A.showTitle = true;
    isc.A.title = "Untitled Window";
    isc.A.headerLabelConstructor = "Label";
    isc.A.headerLabelDefaults = {
        wrap: false,
        align: isc.Canvas.LEFT,
        styleName: "windowHeaderText",
        width: 10,
        inherentWidth: true
    };
    isc.A.showHeaderIcon = true;
    isc.A.headerIconConstructor = "Img";
    isc.A.headerIconDefaults = {width: 16, height: 16, layoutAlign: "center", src: "[SKIN]/Window/headerIcon.gif"};
    isc.A.canFocusInHeaderButtons = false;
    isc.A.showCloseButton = true;
    isc.A.closeButtonConstructor = "ImgButton";
    isc.A.closeButtonDefaults = {
        width: 16,
        height: 14,
        layoutAlign: "center",
        src: "[SKIN]/Window/close.gif",
        click: function () {
            return this.creator.$7e()
        }
    };
    isc.A.showMinimizeButton = true;
    isc.A.minimizeButtonConstructor = "ImgButton";
    isc.A.minimizeButtonDefaults = {
        width: 16, height: 14, layoutAlign: "center", src: "[SKIN]/Window/minimize.gif", click: function () {
            if (!this.creator.onMinimizeClick || (this.creator.onMinimizeClick() != false)) {
                this.creator.minimize()
            }
            return false
        }
    };
    isc.A.minimized = false;
    isc.A.defaultMinimizeHeight = 16;
    isc.A.restoreButtonDefaults = {
        width: 16, height: 14, src: "[SKIN]/Window/restore.gif", layoutAlign: "center", click: function () {
            if (!this.creator.onRestoreClick || (this.creator.onRestoreClick() != false)) {
                this.creator.restore()
            }
            return false
        }
    };
    isc.A.minimized = false;
    isc.A.showMaximizeButton = false;
    isc.A.maximizeButtonConstructor = "ImgButton";
    isc.A.maximizeButtonDefaults = {
        width: 16, height: 14, src: "[SKIN]/Window/maximize.gif", layoutAlign: "center", click: function () {
            if (!this.creator.onMaximizeClick || (this.creator.onMaximizeClick() != false)) {
                this.creator.maximize()
            }
            return false
        }
    };
    isc.A.showFooter = true;
    isc.A.footerConstructor = "HLayout";
    isc.A.footerHeight = 18;
    isc.A.footerControls = ["spacer", "resizer"];
    isc.A.showStatusBar = true;
    isc.A.statusBarConstructor = "Canvas";
    isc.A.statusBarDefaults = {
        overflow: isc.Canvas.HIDDEN,
        styleName: "windowStatusBar",
        addAsChild: true,
        width: "100%",
        wrap: false,
        leftPadding: 5
    };
    isc.A.showResizer = true;
    isc.A.resizerConstructor = "Img";
    isc.A.resizerDefaults = {
        canDragResize: true, getEventEdge: function () {
            if (this.creator.resizeFrom.contains("BR")) {
                return "BR"
            } else if (this.creator.resizeFrom.contains("B")) {
                return "B"
            } else if (this.creator.resizeFrom.contains("R")) {
                return "R"
            }
        }, src: "[SKIN]/Window/resizer.gif", width: 16, height: 16
    };
    isc.A.showToolbar = false;
    isc.A.toolbarConstructor = "Toolbar";
    isc.A.toolbarDefaults = {height: 40, layoutMargin: 10, membersMargin: 5, overflow: "visible"};
    isc.A.customEdges = ["T", "B"];
    isc.A.overflow = "hidden";
    isc.A = isc.Window.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.autoChildParentMap = {
        resizer: "footer",
        statusBar: "footer",
        headerBackground: "header",
        headerIcon: "header",
        headerLabel: "header",
        minimizeButton: "header",
        maximizeButton: "header",
        closeButton: "header",
        toolbar: "body"
    };
    isc.B.push(isc.A.initWidget = function isc_Window_initWidget() {
            if (this.minimized && this.maximized) {
                this.logWarn("Window initialized with maximized and minimized both set to true. " + "This is unsupported. The Window will be rendered minimized.");
                this.maximized = false
            }
            if (this.minimized) {
                this.minimized = null;
                this.minimize()
            } else if (this.maximized) {
                this.maximized = null;
                this.maximize()
            }
            if (this.autoSize) {
                this.vPolicy = "none";
                this.overflow = "visible"
            }
            this.Super(this.$oc);
            if (!this.$7f && this.items != null) {
                for (var i = 0; i < this.items.length; i++) {
                    if (isc.isA.Canvas(this.items[i]) && this.items[i].isDrawn()) this.items[i].clear()
                }
            }
        }
        , isc.A.createChildren = function isc_Window_createChildren() {
            this.makeHeader();
            this.makeBody();
            this.makeToolbar();
            this.makeFooter();
            this.$7f = true
        }
        , isc.A.makeToolbar = function isc_Window_makeToolbar() {
            this.addAutoChild("toolbar", {
                buttons: this.toolbarButtons,
                visibility: this.minimized ? isc.Canvas.HIDDEN : isc.Canvas.INHERIT
            })
        }
        , isc.A.draw = function isc_Window_draw(_1, _2, _3, _4) {
            if (isc.$cv) arguments.$cw = this;
            if (!this.readyToDraw()) return this;
            if (!this.$7f) this.createChildren();
            return this.invokeSuper(isc.Window, "draw", _1, _2, _3, _4)
        }
        , isc.A.destroy = function isc_Window_destroy() {
            if (!this.$7f) {
                var _1 = this.items;
                if (!isc.isAn.Array(_1)) _1 = [_1];
                for (var i = 0; i < _1.length; i++) {
                    if (isc.isA.Canvas(_1[i])) _1[i].destroy()
                }
            }
            this.items = null;
            this.destroyModalMask();
            return this.Super("destroy", arguments)
        }
        , isc.A.mouseUp = function isc_Window_mouseUp() {
            this.bringToFront(true);
            this.Super("mouseUp", arguments)
        }
        , isc.A.makeHeader = function isc_Window_makeHeader() {
            var _1 = this.addAutoChild("header", {styleName: this.headerStyle});
            if (_1 == null) return;
            if (_1 != null) {
                var _2 = this.addAutoChild("headerBackground", {src: this.headerSrc});
                if (_2) _2.sendToBack();
                if (this.minimized) {
                    this.$42l = this.minimizeButtonDefaults;
                    this.$42m = this.minimizeButtonProperties;
                    this.minimizeButtonDefaults = this.restoreButtonDefaults;
                    this.minimizeButtonProperties = this.restoreButtonProperties
                } else if (this.maximized) {
                    this.$42n = this.maximizeButtonDefaults;
                    this.$42o = this.maximizeButtonProperties;
                    this.maximizeButtonDefaults = this.restoreButtonDefaults;
                    this.maximizeButtonProperties = this.restoreButtonProperties
                }
                this.addAutoChildren(this.headerControls, this.header);
                if (this.minimized) {
                    this.minimizeButtonDefaults = this.$42l;
                    this.minimizeButtonProperties = this.$42m;
                    this.$42l = this.$42p = null
                } else if (this.maximized) {
                    this.maximizeButtonDefaults = this.$42n;
                    this.maximizeButtonProperties = this.$42o;
                    this.$42n = this.$42q = null
                }
            }
        }
        , isc.A.setHeaderControls = function isc_Window_setHeaderControls(_1) {
            if (this.headerControls == _1) return;
            var _2 = this.headerControls, _3 = [];
            this.headerControls = _1;
            if (this.header == null) return;
            for (var i = i; i < _2.length; i++) {
                if (isc.isA.String(_2[i])) _3[i] = this[_2[i]];
                else _3[i] = _2[i]
            }
            this.header.removeMembers(_3);
            this.header.addMembers(_1)
        }
        , isc.A.setShowHeaderControl = function isc_Window_setShowHeaderControl(_1, _2, _3) {
            var _4 = this.headerControls;
            if (!_4.contains(_1)) {
                this.logWarn("request to show/hide header control with name:" + _1 + ". No such control is present in this.headerControls - ignoring.");
                return
            }
            if (!_3)
                _3 = "show" + _1.substring(0, 1).toUpperCase() + _1.substring(1);
            if (this[_3] == _2) return;
            this[_3] = _2;
            if (this.header == null) return;
            if (this[_1]) {
                if (_2) this[_1].show(); else this[_1].hide()
            } else if (_2) {
                var _5 = 0;
                for (var i = 0; i < _4.length; i++) {
                    if (_4[i] == _1) break;
                    if (this[_4[i]]) _5++
                }
                this.addAutoChild(_1, null, null, this.header, _5);
                this[_1].show()
            }
        }
        , isc.A.setShowCloseButton = function isc_Window_setShowCloseButton(_1) {
            this.setShowHeaderControl("closeButton", _1, "showCloseButton")
        }
        , isc.A.setShowMinimizeButton = function isc_Window_setShowMinimizeButton(_1) {
            this.setShowHeaderControl("minimizeButton", _1, "showMinimizeButton")
        }
        , isc.A.setShowMaximizeButton = function isc_Window_setShowMaximizeButton(_1) {
            this.setShowHeaderControl("maximizeButton", _1, "showMaximizeButton")
        }
        , isc.A.setShowHeaderIcon = function isc_Window_setShowHeaderIcon(_1) {
            this.setShowHeaderControl("headerIcon", _1, "showHeaderIcon")
        }
        , isc.A.getDynamicDefaults = function isc_Window_getDynamicDefaults(_1) {
            if (isc.endsWith(_1, isc.Button.Class)) {
                return {canFocus: this.canFocusInHeaderButtons}
            }
        }
        , isc.A.headerLabel_autoMaker = function isc_Window_headerLabel_autoMaker() {
            if (!this.showTitle) {
                this.headerLabel = null;
                return
            }
            var _1 = isc.Canvas.create({
                autoDraw: false,
                _generated: true,
                contents: isc.Canvas.blankImgHTML(1000, 100),
                overflow: "hidden"
            });
            var _2 = this.canDragReposition;
            if (_2) {
                _1.canDragReposition = true;
                _1.dragTarget = this;
                this.canDragReposition = false
            }
            var _3 = this.headerLabel = this.createAutoChild("headerLabel", {
                height: "100%", contents: this.title, dragTarget: this, getCurrentCursor: function () {
                    if (this.parentElement)
                        return this.parentElement.getCurrentCursor();
                    return this.Super("getCurrentCursor", arguments)
                }
            });
            _1.addChild(_3);
            this.header.addMember(_1)
        }
        , isc.A.setTitle = function isc_Window_setTitle(_1) {
            if (_1) this.title = _1;
            if (!this.header) return;
            if (this.headerLabel) this.headerLabel.setContents(this.title); else this.header.setContents(this.title)
        }
        , isc.A.setButtons = function isc_Window_setButtons(_1) {
            return this.setToolbarButtons(_1)
        }
        , isc.A.setToolbarButtons = function isc_Window_setToolbarButtons(_1) {
            this.toolbarButtons = _1;
            if (this.toolbar) this.toolbar.setButtons(_1)
        }
        , isc.A.makeFooter = function isc_Window_makeFooter() {
            if (!this.showFooter) return;
            this.addAutoChild("footer", {height: this.footerHeight});
            if (!this.footer) return;
            var _1 = [];
            for (var i = 0; i < this.footerControls.length; i++) {
                var _3 = this.footerControls[i], _4 = {};
                if (_3 == "spacer") _3 = isc.LayoutSpacer.create();
                if (_3 == "resizer") {
                    if (!this.canDragResize) continue;
                    _4.dragTarget = this
                }
                _4.visibility = this.minimized ? isc.Canvas.HIDDEN : isc.Canvas.INHERIT;
                if (isc.isA.String(_3)) {
                    this.addAutoChild(_3, _4, null, this.footer)
                } else {
                    if (isc.isA.Canvas(_3)) _3.setProperties(_4); else isc.addProperties(_3, _4);
                    this.footer.addMember(_3)
                }
            }
            this.addAutoChild("statusBar", {
                height: this.footer.getHeight(),
                visibility: this.minimized ? isc.Canvas.HIDDEN : isc.Canvas.INHERIT
            });
            if (this.status != null) this.setStatus(this.status);
            this.statusBar.sendToBack()
        }
        , isc.A.setStatus = function isc_Window_setStatus(_1) {
            this.status = _1;
            if (this.statusBar == null) return;
            if (_1 == null) _1 = "";
            var _2 = (this.statusBar.leftPadding ? isc.Canvas.spacerHTML(this.statusBar.leftPadding, 1) : "");
            this.statusBar.setContents(_2 + _1)
        }
        , isc.A.setSrc = function isc_Window_setSrc(_1) {
            this.src = _1;
            if (this.body) this.body.setContentsURL(_1)
        }
        , isc.A.makeBody = function isc_Window_makeBody() {
            if (!this.showBody) return;
            var _1, _2, _3;
            if (this.src) {
                _3 = this.src
            } else {
                var _4 = this.items;
                if (isc.isA.Array(_4)) {
                    _1 = _4.duplicate()
                } else if (isc.isA.Canvas(_4)) {
                    _1 = _4
                } else {
                    _2 = _4
                }
                if (!isc.isAn.Array(_4)) _4 = [_4];
                for (var i = 0; i < _4.length; i++) {
                    if (isc.isAn.Object(_4[i])) {
                        _4[i].locatorParent = this;
                        _4[i].$86x = this.ID
                    }
                }
            }
            if (!this.bodyConstructor) {
                if (_3) {
                    this.bodyConstructor = "HTMLFlow"
                } else if (_2) {
                    this.bodyConstructor = "Canvas"
                } else if (!this.autoSize) {
                    if (this.contentLayout != "none") this.bodyConstructor = "Layout"; else this.bodyConstructor = "Canvas"
                } else {
                    this.bodyConstructor = "Layout";
                    var _6 = {vPolicy: "none", hPolicy: "none"};
                    if (!this.bodyProperties) this.bodyProperties = _6; else isc.addProperties(this.bodyProperties, _6)
                }
            }
            this.createCanvii(_1);
            if (isc.Browser.isMoz && _3 != null) {
                if (!this.body) this.body = {};
                this.body.useClipDiv = false
            }
            var _7 = ("body", {
                contents: _2 || "&nbsp;",
                _generated: false,
                defaultHeight: this.autoSize ? 50 : 100,
                contentsURL: _3,
                contentsType: this.contentsType,
                hideUsingDisplayNone: (isc.Browser.isMoz && _3 ? true : false),
                styleName: this.bodyStyle,
                backgroundColor: this.bodyColor,
                visibility: this.minimized ? isc.Canvas.HIDDEN : isc.Canvas.INHERIT,
                vertical: (this.contentLayout == isc.Canvas.VERTICAL),
                overflow: this.autoSize ? "visible" : "auto"
            });
            var _8 = isc.ClassFactory.getClass(this.bodyConstructor);
            if (_8 && _8.isA("Layout")) {
                _7.members = _1
            } else {
                _7.children = _1
            }
            this.addAutoChild("body", _7)
        }
        , isc.A.setBodyColor = function isc_Window_setBodyColor(_1) {
            this.bodyColor = _1;
            if (this.body) this.body.setBackgroundColor(_1)
        }
        , isc.A.hasInherentHeight = function isc_Window_hasInherentHeight() {
            return this.autoSize
        }
        , isc.A.hasInherentWidth = function isc_Window_hasInherentWidth() {
            return this.autoSize
        }
        , isc.A.addItem = function isc_Window_addItem(_1, _2) {
            return this.addItems([_1], _2)
        }
        , isc.A.removeItem = function isc_Window_removeItem(_1) {
            return this.removeItems([_1])
        }
        , isc.A.addItems = function isc_Window_addItems(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            if (!this.items) this.items = [];
            for (var i = 0; i < _1.length; i++) {
                if (!_1[i]) continue;
                if (this.items.contains(_1[i])) continue;
                if (_2 != null) this.items.addAt(_1[i], _2 + i); else this.items.add(_1[i]);
                _1[i].locatorParent = this;
                _1[i].$86x = this.ID;
                if (!this.$7f) {
                    if (isc.isA.Canvas(_1[i]) && _1[i].isDrawn()) _1[i].clear()
                } else {
                    if (this.body.addMember) {
                        this.body.addMember(_1[i], _2 != null ? _2 + i : null)
                    } else {
                        this.body.addChild(_1[i])
                    }
                }
            }
            return _1
        }
        , isc.A.removeItems = function isc_Window_removeItems(_1) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            for (var i = 0; i < _1.length; i++) {
                delete _1[i].locatorParent
            }
            if (this.$7f) {
                if (this.body.removeMembers) this.body.removeMembers(_1); else {
                    for (var i = 0; i < _1.length; i++) {
                        if (_1[i].parentElement == this.body) _1[i].deparent()
                    }
                }
            }
            this.items.removeList(_1);
            return _1
        }
        , isc.A.locatorChildDestroyed = function isc_Window_locatorChildDestroyed(_1) {
            if (this.items && this.items.contains(_1)) this.items.remove(_1)
        }
        , isc.A.replaceItem = function isc_Window_replaceItem(_1, _2) {
            if (_1 == _2) return _1;
            if (_2 == null) return this.removeItem(_1);
            if (_1 == null) return this.addItem(_2);
            for (var i = 0; i < this.items.length; i++) {
                if (this.items[i] == _1) {
                    this.items[i] = _2;
                    if (!this.$7f) {
                        if (isc.isA.Canvas(_2) && _2.isDrawn()) _2.clear()
                    } else {
                        if (this.body.addMember) {
                            var _4 = this.body.getMemberNumber(_1);
                            this.body.removeMember(_1);
                            this.body.addMember(_2, _4)
                        } else {
                            this.body.removeChild(_1);
                            this.body.addChild(_2)
                        }
                    }
                    break
                }
            }
        }
        , isc.A.layoutChildren = function isc_Window_layoutChildren(_1, _2, _3, _4) {
            if (this.body == null) return;
            if (this.$7g) {
                this.$7g = null;
                this.disableAutoSize()
            }
            if (this.autoSize) this.$7h();
            this.invokeSuper(isc.Window, "layoutChildren", _1, _2, _3, _4);
            var _5 = this.edgesAsChild ? this.$l0 : null;
            if (_5) _5.setHeight(this.getVisibleHeight(true))
        }
        , isc.A.$7h = function isc_Window__matchBodyWidth() {
            if (this.minimized) return;
            if (this.$7i) return;
            this.$7i = true;
            var _1 = this.edgesAsChild ? this.$l0 : null;
            if (!this.body.isDrawn()) this.body.draw();
            this.body.inherentWidth = true;
            var _2 = (this.getWidth() - this.getInnerWidth()) + this.$tb + this.$tc;
            if (_1) _2 += _1.$tb + _1.$tc;
            var _3 = this.body.getVisibleWidth() + _2;
            this.logInfo("edgeWidth is: " + _2 + ", setting window width to: " + _3, "layout");
            if (this.getWidth() != _3) this.setWidth(_3);
            this.$7i = null
        }
        , isc.A.disableAutoSize = function isc_Window_disableAutoSize() {
            this.setAutoSize(false)
        }
        , isc.A.setAutoSize = function isc_Window_setAutoSize(_1) {
            this.autoSize = _1;
            if (_1) {
                if (this.body) {
                    if (isc.isA.Layout(this.body)) this.body.vPolicy = this.body.hPolicy = "none";
                    this.body.setOverflow("visible")
                }
                this.vPolicy = "none";
                this.setOverflow("visible")
            } else {
                if (this.body) {
                    if (isc.isA.Layout(this.body)) this.body.vPolicy = this.body.hPolicy = "fill";
                    this.body.setOverflow("auto");
                    this.body.inherentWidth = false
                }
                this.vPolicy = "fill";
                this.setOverflow("hidden")
            }
        }
        , isc.A.dragResizeStart = function isc_Window_dragResizeStart() {
            if (this.Super("dragResizeStart", arguments) == false) return;
            if (this.autoSize && isc.EH.dragTarget == this) {
                this.autoSize = false;
                this.$7g = true
            }
        }
        , isc.A.returnValue = function isc_Window_returnValue(_1) {
            if (this.isVisible()) this.hide();
            if (this.callback) {
                this.delayCall("fireCallback", [this.callback, "value", [_1]], 50)
            }
            return _1
        }
        , isc.A.show = function isc_Window_show(_1, _2, _3, _4) {
            if (isc.$cv) arguments.$cw = this;
            if (this.isModal) {
                if (this.modalTarget) {
                    if (!isc.isA.Canvas(this.modalTarget) || this.modalTarget.contains(this)) {
                        this.logWarn("Invalid modalTarget:" + this.modalTarget + ". Should be a canvas, and not an ancestor of this Window.");
                        delete this.modalTarget;
                        this.isModal = false
                    } else {
                        this.modalTarget.showComponentMask(this.showModalMask ? {
                            styleName: this.modalMaskStyle,
                            opacity: this.modalMaskOpacity
                        } : null);
                        this.observeModalTarget()
                    }
                } else if (this.topElement != null) {
                    this.logWarn("Window specified with 'isModal' set to true, but this window has a " + "parentElement. Only top level Windows can be shown modally.");
                    this.isModal = false
                } else {
                    this.showClickMask(this.getID() + (this.dismissOnOutsideClick ? ".handleCloseClick()" : ".flash()"), false, [this]);
                    this.makeModalMask()
                }
            }
            if (this.autoCenter && !this.parentElement) {
                this.$7j = true;
                this.moveTo(0, -1000);
                this.$7j = false
            }
            this.invokeSuper(isc.Window, "show", _1, _2, _3, _4);
            if (this.autoCenter) {
                this.centerInPage();
                if (!this.parentElement) {
                    isc.Page.setEvent(this.$nx, this, null, "parentResized")
                }
            }
            this.bringToFront(true)
        }
        , isc.A.makeModalMask = function isc_Window_makeModalMask() {
            if (!this.showModalMask) return;
            if (!this.modalMask) this.modalMask = this.createAutoChild("modalMask", {
                styleName: this.modalMaskStyle,
                opacity: this.modalMaskOpacity
            });
            this.modalMask.show()
        }
        , isc.A.hideModalMask = function isc_Window_hideModalMask() {
            if (this.modalMask) this.modalMask.hide()
        }
        , isc.A.destroyModalMask = function isc_Window_destroyModalMask() {
            if (this.modalMask) {
                this.modalMask.destroy();
                this.modalMask = null
            }
        }
        , isc.A.observeModalTarget = function isc_Window_observeModalTarget() {
            if (this.$550) return;
            this.observe(this.modalTarget, "show", "observer.modalTargetVisibilityChanged(observed)");
            this.observe(this.modalTarget, "hide", "observer.modalTargetVisibilityChanged(observed)");
            this.observe(this.modalTarget, "clear", "observer.modalTargetVisibilityChanged(observed)");
            this.observe(this.modalTarget, "draw", "observer.modalTargetVisibilityChanged(observed)");
            this.observe(this.modalTarget, "parentVisibilityChanged", "observer.modalTargetVisibilityChanged(observed)")
        }
        , isc.A.ignoreModalTarget = function isc_Window_ignoreModalTarget() {
            if (this.$550) return;
            this.ignore(this.modalTarget, "show");
            this.ignore(this.modalTarget, "hide");
            this.ignore(this.modalTarget, "draw");
            this.ignore(this.modalTarget, "clear");
            this.ignore(this.modalTarget, "parentVisibilityChanged")
        }
        , isc.A.modalTargetVisibilityChanged = function isc_Window_modalTargetVisibilityChanged(_1) {
            this.$550 = true;
            if (_1.isVisible() && _1.isDrawn()) this.show(); else this.hide();
            delete this.$550
        }
        , isc.A.shouldDismissOnEscape = function isc_Window_shouldDismissOnEscape() {
            if (this.dismissOnEscape != null) return this.dismissOnEscape;
            return this.showHeader && this.headerControls && this.showCloseButton && this.headerControls.contains("closeButton")
        }
        , isc.A.handleKeyPress = function isc_Window_handleKeyPress() {
            var _1 = isc.EH.getKey();
            if (_1 == "Escape" && this.shouldDismissOnEscape()) {
                this.handleEscape();
                return false
            }
            return this.Super("handleKeyPress", arguments)
        }
        , isc.A.handleEscape = function isc_Window_handleEscape() {
            if (this.isMasked()) return;
            this.handleCloseClick()
        }
        , isc.A.resized = function isc_Window_resized(_1, _2, _3, _4) {
            this.invokeSuper(isc.Window, "resized", _1, _2, _3, _4);
            if (this.autoCenter) this.centerInPage()
        }
        , isc.A.hide = function isc_Window_hide(_1, _2, _3, _4) {
            if (this.$7k) isc.Animation.finishAnimation(this.$7k);
            this.invokeSuper(isc.Window, "hide", _1, _2, _3, _4);
            if (this.isDrawn() && this.isModal) {
                if (this.modalTarget) {
                    this.modalTarget.hideComponentMask();
                    this.ignoreModalTarget()
                } else {
                    this.hideClickMask();
                    this.hideModalMask()
                }
            }
        }
        , isc.A.clear = function isc_Window_clear(_1, _2, _3, _4) {
            if (this.$7k) isc.Animation.finishAnimation(this.$7k);
            this.invokeSuper(isc.Window, "clear", _1, _2, _3, _4);
            if (!this.clearingWithModalTarget && this.isVisible() && this.isModal) {
                if (this.modalTarget) {
                    this.ignoreModalTarget();
                    this.modalTarget.hideComponentMask()
                } else {
                    this.hideClickMask();
                    this.hideModalMask()
                }
            }
        }
        , isc.A.parentResized = function isc_Window_parentResized() {
            this.Super("parentResized", arguments);
            if (this.autoCenter) this.centerInPage()
        }
        , isc.A.handleMoved = function isc_Window_handleMoved() {
            this.Super("handleMoved", arguments);
            if (this.isDrawn() && !this.$7j) this.autoCenter = false
        }
        , isc.A.centerInPage = function isc_Window_centerInPage() {
            var _1 = this.getVisibleWidth(), _2 = this.getVisibleHeight(),
                _3 = this.parentElement ? this.parentElement : isc.Page,
                _4 = ((_3.getWidth() - _1) / 2) + _3.getScrollLeft(),
                _5 = ((_3.getHeight() - _2) / 2) + _3.getScrollTop();
            _4 = Math.round(_4);
            _5 = Math.max(Math.round(_5), 0);
            this.$7j = true;
            this.moveTo(_4, _5);
            this.$7j = null
        }
        , isc.A.flash = function isc_Window_flash(_1) {
            var _2 = this.showHeader;
            if (_1 == null) {
                if (this.$7l) return false;
                this.$7l = true;
                _1 = 0;
                if (_2) {
                    this.$7m = this.header.getStateName();
                    if (this.headerBackground) {
                        this.$7n = this.headerBackground.getStateName();
                        this.$7o = this.headerBackground.src
                    }
                } else {
                    this.$7p = this.body.backgroundColor
                }
            }
            if (_2) {
                var _3 = (_1 % 2 == 0 ? this.hiliteHeaderStyle : this.$7m),
                    _4 = (_1 % 2 == 0 ? this.hiliteHeaderSrc : this.$7o),
                    _5 = (_1 % 2 == 0 ? this.hiliteHeaderStyle : this.$7n);
                this.header.setStyleName(_3);
                var _6 = this.headerBackground;
                if (_6) {
                    this.headerBackground.setStyleName(_5);
                    if (_6.setSrc) _6.setSrc(_4)
                }
            } else {
                var _7 = (_1 % 2 == 0 ? this.hiliteBodyColor : this.$7p);
                this.body.setBackgroundColor(_7)
            }
            _1++;
            if (_1 < 4) this.delayCall("flash", [_1], 100); else this.$7l = false;
            return false
        }
        , isc.A.minimize = function isc_Window_minimize() {
            if (this.$7k) isc.Animation.finishAnimation(this.$7k);
            if (this.minimized) return;
            if (!this.maximized) {
                this.$7q = this.getHeight();
                this.$7r = this.getVisibleHeight();
                this.$7s = this.$po;
                this.$7t = this.canDragResize;
                this.canDragResize = false
            } else {
                if (this.maximizeButton) {
                    this.maximizeButton.addProperties(this.maximizeButtonDefaults);
                    this.maximizeButton.redraw()
                }
            }
            var _1;
            if (this.minimizeHeight) {
                _1 = this.minimizeHeight
            } else if (this.showHeader) {
                var _2;
                if (this.header) {
                    _2 = this.header.getHeight()
                } else {
                    var _3 = this.headerDefaults;
                    _2 = _3.height || _3.defaultHeight
                }
                _1 = _2 + (this.layoutMargin * 2) + this.getVMarginBorderPad()
            } else {
                _1 = this.defaultMinimizeHeight
            }
            if (this.overflow == isc.Canvas.VISIBLE) {
                this.setHeight(this.getVisibleHeight())
            }
            this.$7u = this.overflow;
            this.setOverflow("hidden");
            var _4 = this.minimizeButton;
            if (_4) {
                _4.addProperties(this.restoreButtonDefaults);
                _4.markForRedraw()
            }
            this.$7v = _1;
            if (this.animateMinimize && this.isDrawn() && this.isVisible()) {
                if (_4) {
                    _4.disable();
                    _4.redraw()
                }
                this.$7w();
                this.$7k = isc.Animation.registerAnimation(this.animateMinimizeStep, (this.minimizeTime || this.animateTime), this.minimizeAcceleration || this.animateAcceleration, this)
            } else {
                this.completeMinimize(_1)
            }
        }
        , isc.A.$7w = function isc_Window__storeContentRestoreStats() {
            if (this.body) {
                this.$7x = this.body.getScrollTop();
                this.$7y = this.body.overflow;
                this.$7z = this.body.getHeight();
                this.$70 = this.body.getWidth();
                this.$71 = this.body.$po;
                this.$72 = this.body.$pn;
                if (this.$7y == isc.Canvas.VISIBLE) {
                    this.body.resizeTo(this.body.getVisibleWidth(), this.body.getVisibleHeight())
                }
                this.body.setOverflow(isc.Canvas.HIDDEN)
            }
            if (this.footer) {
                this.$73 = this.footer.overflow;
                if (this.$73 == isc.Canvas.VISIBLE) {
                    this.footer.setHeight(this.footer.getVisibleHeight())
                }
                this.footer.setOverflow(isc.Canvas.HIDDEN)
            }
        }
        , isc.A.$74 = function isc_Window__resetContentRestoreStats() {
            if (this.body) {
                this.body.scrollTo(null, this.$7x, "restore");
                this.body.resizeTo(this.$70, this.$7z);
                this.body.$po = this.$71;
                this.body.$pn = this.$72;
                this.body.setOverflow(this.$7y)
            }
            if (this.footer) {
                this.footer.scrollTo(null, 0, "restore");
                this.footer.setHeight(this.footerHeight);
                this.footer.setOverflow(this.$73)
            }
            delete this.$7x;
            delete this.$7z;
            delete this.$71;
            delete this.$70;
            delete this.$72;
            delete this.$7y;
            delete this.$73
        }
        , isc.A.animateMinimizeStep = function isc_Window_animateMinimizeStep(_1, _2, _3, _4, _5) {
            var _6 = (!_4 && !_5);
            if (this.maximized && !this.$42u) {
                this.$42v = (this.parentElement ? this.parentElement.getInnerHeight() : isc.Page.getHeight());
                this.$42w = (this.parentElement ? this.parentElement.getInnerWidth() : isc.Page.getWidth());
                this.$42u = true
            }
            var _7 = this.minimized ? this.$7v : this.maximized ? this.$42v : this.$7r,
                _8 = _4 ? this.$7r : _5 ? this.$42v : this.$7v, _9 = this.maximized ? this.$42w : this.$42x,
                _10 = _5 ? this.$42w : this.$42x;
            var _11 = Math.round(_7 + (_1 * (_8 - _7))), _12 = (_10 == _9 ? _10 : Math.round(_9 + (_1 * (_10 - _9))));
            var _13 = _11 - this.getVMarginBorder() - (2 * this.layoutMargin) -
                (this.showHeader ? this.header.getHeight() + this.membersMargin : 0),
                _14 = (this.showBody ? this.body : null), _15 = (this.showFooter ? this.footer : null), _16 = 0,
                _17 = 0, _18 = (_15 ? this.footerHeight : 0), _19 = this.membersMargin || 0;
            if (_15 != null) {
                if (_13 <= _18) {
                    _16 = _13
                } else {
                    _16 = _18
                }
            }
            var _20 = _15 ? _18 + _19 : 0;
            if (_14 != null && (_13 > _20)) {
                _17 = _13 - _20
            }
            if (_15) {
                if (_16 > 0) {
                    if (_15.getHeight() != _16) {
                        var _21 = _15.getScrollTop() + _15.getViewportHeight();
                        _15.resizeTo(null, _16);
                        _15.scrollTo(null, _21 - _15.getViewportHeight(), "animateMinimize")
                    }
                    if (!_15.isVisible()) _15.show()
                } else if (_15.isVisible()) {
                    _15.hide()
                }
            }
            if (_14) {
                if (_17 > 0) {
                    if (_14.getHeight() != _17) {
                        var _21 = _14.getScrollTop() + _14.getViewportHeight();
                        _14.resizeTo(null, _17);
                        _14.scrollTo(null, _21 - _14.getViewportHeight(), "animateMinimize")
                    }
                    if (!_14.isVisible()) _14.show()
                } else if (_14.isVisible()) {
                    _14.hide()
                }
            }
            if (_5 || this.maximized) {
                var _22 = (_5 ? this.$42y : 0), _23 = (_5 ? this.$42z : 0), _24 = (_5 ? 0 : this.$42y),
                    _25 = (_5 ? 0 : this.$42z);
                this.moveTo(Math.round(_22 + (_1 * (_24 - _22))), Math.round(_23 + (_1 * (_25 - _23))), true)
            }
            this.resizeBy((_12 - this.getWidth()), (_11 - this.getHeight()), null, null, true);
            if (_1 == 1) {
                delete this.$42u;
                this.$74();
                delete this.$7k;
                if (_4) this.completeRestore(true); else if (_5) this.completeMaximize(true); else this.completeMinimize(this.$7v, true)
            }
        }
        , isc.A.animateRestoreStep = function isc_Window_animateRestoreStep(_1, _2, _3) {
            this.animateMinimizeStep(_1, _2, _3, true)
        }
        , isc.A.animateMaximizeStep = function isc_Window_animateMaximizeStep(_1, _2, _3) {
            this.animateMinimizeStep(_1, _2, _3, null, true)
        }
        , isc.A.isAnimating = function isc_Window_isAnimating(_1, _2, _3, _4, _5) {
            if (this.invokeSuper(isc.Window, "isAnimating", _1, _2, _3, _4, _5)) return true;
            if (_1 && !isc.isAn.Array(_1)) _1 = [_1];
            if (this.$7k && ((_1 == null) || (_1.contains("minimize")) || (_1.contains("rect")))) return true;
            return false
        }
        , isc.A.completeMinimize = function isc_Window_completeMinimize(_1, _2) {
            this.minimized = true;
            this.maximized = false;
            if (this.body && this.body.isVisible()) this.body.hide();
            if (this.footer && this.footer.isVisible()) this.footer.hide();
            this.$po = _1;
            if (this.$420 != null) {
                if (!_2) this.setWidth(this.$420);
                this.$pn = this.$420
            }
            if (!_2) {
                this.setHeight(_1);
                if (this.$42y != null) this.setLeft(this.$42y);
                if (this.$42z != null) this.setTop(this.$42z)
            }
            if (this.$421 != null) this.setShowShadow(this.$421);
            if (this.$422 != null && this.headerLabel)
                this.headerLabel.parentElement.canDragReposition = this.$422;
            delete this.$422;
            delete this.$42z;
            delete this.$42y;
            delete this.$421;
            delete this.$420;
            if (this.minimizeButton) this.minimizeButton.enable()
        }
        , isc.A.restore = function isc_Window_restore() {
            if (this.$7k) {
                isc.Animation.finishAnimation(this.$7k)
            }
            if (!this.minimized && !this.maximized) return;
            if (!this.$7r) this.$7r = this.getVisibleHeight();
            var _1 = (this.minimized ? this.minimizeButton : this.maximizeButton);
            if (_1) {
                _1.addProperties(this.minimized ? this.minimizeButtonDefaults : this.maximizeButtonDefaults);
                _1.markForRedraw()
            }
            if (this.animateMinimize && this.isDrawn() && this.isVisible()) {
                if (_1) {
                    _1.disable();
                    _1.redraw()
                }
                this.$7w();
                this.$7k = isc.Animation.registerAnimation(this.animateRestoreStep, (this.minimizeTime || this.animateTime), this.minimizeAcceleration || this.animateAcceleration, this)
            } else {
                this.completeRestore()
            }
        }
        , isc.A.completeRestore = function isc_Window_completeRestore(_1) {
            if (this.$7u != null) this.setOverflow(this.$7u);
            if (this.$7q != null) this.setHeight(this.$7q);
            if (this.$420 != null) this.setWidth(this.$420);
            if (!_1) {
                if (this.$42y != null) this.setLeft(this.$42y);
                if (this.$42z != null) this.setTop(this.$42z)
            }
            if (this.$po != null) this.$po = this.$7s;
            if (this.$pn != null) this.$pn = this.$423;
            if (this.$421 != null) this.setShowShadow(this.$421);
            if (this.$7t != null) this.canDragResize = this.$7t;
            if (this.$422 != null && this.headerLabel)
                this.headerLabel.parentElement.canDragReposition = this.$422;
            var _2 = this.minimized ? this.minimizeButton : this.maximizeButton;
            this.minimized = false;
            this.maximized = false;
            this.$75();
            if (this.$424) {
                this.reflowNow();
                this.setAutoSize(true)
            }
            delete this.$7q;
            delete this.$7s;
            delete this.$7r;
            delete this.$7t;
            delete this.$422;
            delete this.$7u;
            delete this.$420;
            delete this.$423;
            delete this.$421;
            delete this.$42y;
            delete this.$42z;
            delete this.$424;
            if (_2) _2.enable()
        }
        , isc.A.$75 = function isc_Window__showComponents() {
            if (this.body && !this.body.isVisible()) this.body.show();
            if (this.footer && !this.footer.isVisible()) this.footer.show()
        }
        , isc.A.maximize = function isc_Window_maximize() {
            if (this.$7k) isc.Animation.finishAnimation(this.$7k);
            if (this.maximized) return;
            if (!this.minimized) {
                this.$7q = this.getHeight();
                this.$7r = this.getVisibleHeight();
                this.$7s = this.$po;
                this.$7t = this.canDragResize;
                this.canDragResize = false
            } else {
                if (this.minimizeButton) {
                    this.minimizeButton.addProperties(this.minimizeButtonDefaults);
                    this.minimizeButton.redraw()
                }
            }
            this.$42y = this.getLeft();
            this.$42z = this.getTop();
            this.$420 = this.getWidth();
            this.$42x = this.getVisibleWidth();
            this.$423 = this.$pn;
            if (this.headerLabel) {
                this.$422 = this.headerLabel.parentElement.canDragReposition;
                this.headerLabel.parentElement.canDragReposition = false
            }
            this.$421 = this.showShadow;
            this.setShowShadow(false);
            if (this.autoSize) {
                this.$424 = true;
                this.setAutoSize(false)
            }
            var _1 = this.maximizeButton;
            if (_1) {
                _1.addProperties(this.restoreButtonDefaults);
                _1.markForRedraw()
            }
            if (this.animateMinimize && this.isDrawn() && this.isVisible()) {
                if (_1) {
                    _1.disable();
                    _1.redraw()
                }
                this.$42v = (this.parentElement ? this.parentElement.getInnerHeight() : isc.Page.getHeight());
                this.$42w = (this.parentElement ? this.parentElement.getInnerWidth() : isc.Page.getWidth());
                this.$7w();
                this.$7k = isc.Animation.registerAnimation(this.animateMaximizeStep, (this.minimizeTime || this.animateTime), this.minimizeAcceleration || this.animateAcceleration, this)
            } else {
                this.completeMaximize()
            }
        }
        , isc.A.completeMaximize = function isc_Window_completeMaximize(_1) {
            if (!_1) this.moveTo(0, 0);
            this.resizeTo("100%", "100%");
            this.$75();
            this.minimized = false;
            this.maximized = true;
            if (this.maximizeButton) this.maximizeButton.enable()
        }
        , isc.A.resizeTo = function isc_Window_resizeTo(_1, _2, _3, _4, _5) {
            if (!_5 && this.$7k) {
                isc.Animation.finishAnimation(this.$7k)
            }
            return this.invokeSuper(isc.Window, "resizeTo", _1, _2, _3, _4, _5)
        }
        , isc.A.resizeBy = function isc_Window_resizeBy(_1, _2, _3, _4, _5) {
            if (!_5 && this.$7k) {
                isc.Animation.finishAnimation(this.$7k)
            }
            return this.invokeSuper(isc.Window, "resizeBy", _1, _2, _3, _4, _5)
        }
        , isc.A.$7e = function isc_Window__closeButtonClick() {
            return this.handleCloseClick()
        }
        , isc.A.handleCloseClick = function isc_Window_handleCloseClick() {
            if (this.onCloseClick && this.onCloseClick() == false) return;
            return this.closeClick()
        }
        , isc.A.closeClick = function isc_Window_closeClick() {
            this.returnValue(null);
            this.hide();
            return false
        }
    );
    isc.B._maxIndex = isc.C + 72;
    isc.Window.registerStringMethods({onMaximizeClick: "", onMinimizeClick: "", onRestoreClick: "", onCloseClick: ""});
    if (isc.definePrintWindow) isc.definePrintWindow();
    isc.Window.registerDupProperties("items");
    isc.defineClass("Portlet", "Window");
    isc.A = isc.Portlet.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.showShadow = false;
    isc.A.animateMinimize = true;
    isc.A.dragAppearance = "outline";
    isc.A.canDrop = true;
    isc.A.overflow = "hidden";
    isc.A.minHeight = 60;
    isc.A.minWidth = 70;
    isc.A.resizeFrom = ["T", "B", "L", "R"];
    isc.A.showMaximizeButton = true;
    isc.A.headerControls = ["headerLabel", "minimizeButton", "maximizeButton", "closeButton"];
    isc.A.dragOpacity = 30;
    isc.A.showCloseConfirmationMessage = true;
    isc.A.closeConfirmationMessage = "Close portlet?";
    isc.B.push(isc.A.setMinHeight = function isc_Portlet_setMinHeight(_1) {
            this.minHeight = _1;
            if (this.portalRow) this.portalRow.$87w()
        }
        , isc.A.setMinWidth = function isc_Portlet_setMinWidth(_1) {
            if (this.minWidth == _1) return;
            this.minWidth = _1;
            if (this.portalRow) this.portalRow.reflow("Portlet minWidth changed")
        }
        , isc.A.setRowHeight = function isc_Portlet_setRowHeight(_1) {
            this.rowHeight = _1;
            if (this.portalRow) this.portalRow.setHeight(_1)
        }
        , isc.A.closeClick = function isc_Portlet_closeClick() {
            if (this.showCloseConfirmationMessage) {
                isc.confirm(this.closeConfirmationMessage, {target: this, methodName: "confirmedClosePortlet"})
            } else {
                this.confirmedClosePortlet(true)
            }
        }
        , isc.A.confirmedClosePortlet = function isc_Portlet_confirmedClosePortlet(_1) {
            if (!_1) return;
            if (this.editContext && this.editNode) {
                this.editContext.removeNode(this.editNode)
            } else {
                if (this.portalRow) {
                    this.portalRow.removePortlets(this)
                } else {
                    this.clear()
                }
            }
            if (this.destroyOnClose) this.markForDestroy()
        }
        , isc.A.maximize = function isc_Portlet_maximize() {
            var _1 = this.getVisibleWidth(), _2 = this.getVisibleHeight(), _3 = this.getPageLeft(),
                _4 = this.getPageTop();
            this.$55d = isc.Canvas.create({
                width: this.getVisibleWidth(),
                height: this.getVisibleHeight(),
                minHeight: this.getMinHeight(),
                minWidth: this.getMinWidth(),
                $858: this
            });
            this.masterLayout = this.parentElement;
            this.masterLayout.portletMaximizing = true;
            this.masterLayout.replaceMember(this, this.$55d, false);
            this.masterLayout.portletMaximizing = false;
            this.setWidth(_1);
            this.setHeight(_2);
            this.moveTo(_3, _4);
            this.bringToFront();
            this.draw();
            this.delayCall("doMaximize")
        }
        , isc.A.completeRestore = function isc_Portlet_completeRestore() {
            this.Super("completeRestore", arguments);
            this.masterLayout.portletMaximizing = true;
            this.masterLayout.replaceMember(this.$55d, this);
            this.masterLayout.portletMaximizing = false;
            this.$55d.$858 = null;
            this.$55d.destroy();
            delete this.$55d;
            delete this.masterLayout
        }
        , isc.A.doMaximize = function isc_Portlet_doMaximize() {
            this.Super("maximize", arguments)
        }
    );
    isc.B._maxIndex = isc.C + 8;
    isc.defineClass("PortalColumnHeader", "HLayout");
    isc.A = isc.PortalColumnHeader.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.height = 20;
    isc.A.noResizer = true;
    isc.A.border = "1px solid #CCCCCC";
    isc.A.canDragReposition = true;
    isc.B.push(isc.A.initWidget = function isc_PortalColumnHeader_initWidget() {
            this.Super("initWidget", arguments);
            this.dragTarget = this.creator;
            this.addMember(isc.LayoutSpacer.create());
            this.menu = this.getMenuConstructor().create({
                width: 150,
                portalColumn: this.creator,
                data: [{
                    title: "Remove Column", click: "menu.portalColumn.removeSelf()", enableIf: function (_1, _2, _3) {
                        return _2.portalColumn.portalLayout.getMembers().length > 1
                    }
                }, {title: "Add Column", click: "menu.portalColumn.addNewColumn()"}]
            });
            this.addMember(isc.MenuButton.create({title: "Column Properties", width: 150, menu: this.menu}));
            this.addMember(isc.LayoutSpacer.create())
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.defineClass("PortalRow", "Layout");
    isc.A = isc.PortalRow.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.vertical = false;
    isc.A.respectSizeLimits = true;
    isc.A.overflow = "auto";
    isc.A.membersMargin = 3;
    isc.A.canAcceptDrop = true;
    isc.A.dropLineThickness = 2;
    isc.A.dropLineProperties = {backgroundColor: "blue"};
    isc.A.hDropOffset = 15;
    isc.B.push(isc.A.initWidget = function isc_PortalRow_initWidget() {
            this.Super("initWidget", arguments);
            if (this.portlets) this.addPortlets(this.portlets);
            this.portlets = null
        }
        , isc.A.setCanResizePortlets = function isc_PortalRow_setCanResizePortlets(_1) {
            this.canResizePortlets = _1;
            this.getPortlets().map(function (_2) {
                _2.canDragResize = _1
            })
        }
        , isc.A.prepareForDragging = function isc_PortalRow_prepareForDragging() {
            var _1 = this.ns.EH;
            if (this.hasMember(_1.dragTarget) && _1.dragOperation == _1.DRAG_RESIZE) {
                switch (_1.resizeEdge) {
                    case"B":
                        _1.dragTarget = this;
                        break;
                    case"T":
                        var _2 = this.parentElement.getMemberNumber(this);
                        if (_2 > 0) {
                            _1.dragTarget = this.parentElement.getMember(_2 - 1);
                            _1.resizeEdge = "B"
                        } else {
                            _1.dragTarget = null
                        }
                        break;
                    case"L":
                        var _2 = this.getMemberNumber(_1.dragTarget);
                        if (_2 > 0) {
                            _1.dragTarget = this.getMember(_2 - 1);
                            _1.resizeEdge = "R"
                        } else {
                            var _3 = this.portalLayout.getPortalColumnNumber(this.portalColumn);
                            if (_3 == 0) {
                                _1.dragTarget = null
                            }
                        }
                        break;
                    default:
                        return this.Super("prepareForDragging", arguments)
                }
            } else {
                return this.Super("prepareForDragging", arguments)
            }
        }
        , isc.A.isHDrop = function isc_PortalRow_isHDrop() {
            var _1 = this.getDropPosition();
            var _2 = this.getMember(_1 == 0 ? 0 : _1 - 1);
            if (!_2.containsEvent() && _1 < this.members.length) {
                _2 = this.getMember(_1)
            }
            var _3 = _2.getOffsetX();
            if (_3 < this.hDropOffset || _3 > _2.getVisibleWidth() - this.hDropOffset) {
                return true
            } else {
                return false
            }
        }
        , isc.A.isPortalColumnDrop = function isc_PortalRow_isPortalColumnDrop() {
            var _1 = this.ns.EH.dragTarget;
            var _2 = _1.getDragType();
            if (_2 == "PortalColumn") return true;
            if (_1.isA("Palette")) {
                var _3 = _1.getDragData(), _4 = (isc.isAn.Array(_3) ? _3[0] : _3);
                if (_4.className == "PortalColumn" || _4.type == "PortalColumn") return true
            }
            return false
        }
        , isc.A.dropMove = function isc_PortalRow_dropMove() {
            if (this.isHDrop() && !this.isPortalColumnDrop()) {
                this.Super("dropMove", arguments);
                this.parentElement.hideDropLine();
                return isc.EH.STOP_BUBBLING
            } else {
                this.hideDropLine()
            }
        }
        , isc.A.dropOver = function isc_PortalRow_dropOver() {
            if (this.isHDrop() && !this.isPortalColumnDrop()) {
                this.Super("dropOver", arguments);
                this.parentElement.hideDropLine();
                return isc.EH.STOP_BUBBLING
            } else {
                this.hideDropLine()
            }
        }
        , isc.A.getDropComponent = function isc_PortalRow_getDropComponent(_1, _2) {
            var _3 = this.portalLayout.getDropPortlet(_1, this.portalLayout.getPortalColumnNumber(this.portalColumn), this.portalColumn.getPortalRowNumber(this), _2);
            if (this.handleDroppedEditNode) _3 = this.handleDroppedEditNode(_3, _2);
            if (_3) {
                if (!isc.isA.Portlet(_3)) {
                    _3 = isc.Portlet.create({autoDraw: false, title: "", items: _3, destroyOnClose: true})
                }
            }
            return _3
        }
        , isc.A.drop = function isc_PortalRow_drop() {
            if (this.isHDrop() && !this.isPortalColumnDrop()) {
                this.Super("drop", arguments);
                this.parentElement.hideDropLine();
                this.hideDropLine();
                return isc.EH.STOP_BUBBLING
            } else {
                this.hideDropLine()
            }
        }
        , isc.A.setMinHeight = function isc_PortalRow_setMinHeight(_1) {
            if (this.minHeight == _1) return;
            this.minHeight = _1;
            this.portalColumn.rowLayout.reflow("PortalRow minHeight changed")
        }
        , isc.A.$87w = function isc_PortalRow__checkPortletMinHeight() {
            this.setMinHeight(this.getMembers().map("getMinHeight").max() + this.$2u() + this.getVMarginBorder())
        }
        , isc.A.reflow = function isc_PortalRow_reflow() {
            this.portalLayout.reflow();
            this.Super("reflow", arguments)
        }
        , isc.A.$87x = function isc_PortalRow__getDesiredMemberSpace() {
            return this.members.map(function (_1) {
                if (isc.isA.Number(_1.$pn)) {
                    return Math.max(_1.$pn, _1.minWidth)
                } else {
                    return _1.minWidth
                }
            }).sum()
        }
        , isc.A.getTotalMemberSpace = function isc_PortalRow_getTotalMemberSpace() {
            var _1 = this.Super("getTotalMemberSpace", arguments);
            var _2 = this.$87x();
            if (_1 < _2) {
                return _2
            } else {
                return _1
            }
        }
        , isc.A.membersChanged = function isc_PortalRow_membersChanged() {
            if (this.members.length == 0) {
                if (!this.portletMaximizing) {
                    if (this.editContext && this.editNode) this.editContext.removeNode(this.editNode);
                    this.destroy()
                }
            } else {
                this.$87w()
            }
        }
        , isc.A.addMembers = function isc_PortalRow_addMembers(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _3 = this;
            _1.map(function (_6) {
                if (_6.$858) return;
                _6.canDragResize = _3.canResizePortlets;
                var _4 = _6.$po;
                if (_4) {
                    _6.$po = null;
                    _6._percent_height = null;
                    if (!_6.rowHeight) _6.rowHeight = _4
                }
                if (_6.rowHeight) {
                    if (!_3.$po) {
                        _3.setHeight(_6.rowHeight);
                        _3.$po = _6.rowHeight
                    }
                }
            });
            this.Super("addMembers", arguments);
            _1.map(function (_6) {
                _6.portalRow = _3
            });
            if (this.editContext && !this.$86r && !this.portletMaximizing) {
                for (var i = 0; i < _1.length; i++) {
                    var _6 = _1[i];
                    if (_6.editNode) {
                        this.editContext.addNode(_6.editNode, this.editNode, _2 + i, null, true)
                    }
                }
            }
        }
        , isc.A.addPortlets = function isc_PortalRow_addPortlets(_1, _2) {
            this.$86r = true;
            this.addMembers(_1, _2);
            delete this.$86r
        }
        , isc.A.addPortlet = function isc_PortalRow_addPortlet(_1, _2) {
            this.addPortlets(_1, _2)
        }
        , isc.A.removeMembers = function isc_PortalRow_removeMembers(_1) {
            this.Super("removeMembers", arguments);
            if (!isc.isAn.Array(_1)) _1 = [_1];
            if (!this.portletMaximizing) {
                var _2 = this;
                _1.map(function (_3) {
                    if (_3.portalRow) _3.portalRow = null;
                    if (_2.editContext && _3.editNode && !_2.$86s) {
                        _2.editContext.removeNode(_3.editNode, true)
                    }
                })
            }
        }
        , isc.A.removePortlets = function isc_PortalRow_removePortlets(_1) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _2 = this;
            _1.map(function (_4) {
                var _3 = _4.$55d;
                if (_3) {
                    _2.removeMembers(_3);
                    delete _3.$858;
                    delete _4.$55d;
                    _3.destroy();
                    _4.deparent();
                    _4.clear();
                    _4.portalRow = null
                } else {
                    this.$86s = true;
                    _2.removeMembers(_4);
                    delete this.$86s
                }
            })
        }
        , isc.A.removePortlet = function isc_PortalRow_removePortlet(_1) {
            this.removePortlets(_1)
        }
        , isc.A.getPortlet = function isc_PortalRow_getPortlet(_1) {
            return this.getMember(_1)
        }
        , isc.A.getPortlets = function isc_PortalRow_getPortlets() {
            return this.getMembers().map(function (_1) {
                if (_1.$858) {
                    return _1.$858
                } else {
                    return _1
                }
            })
        }
    );
    isc.B._maxIndex = isc.C + 23;
    isc.A = isc.PortalRow;
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.applyStretchResizePolicy = function isc_c_PortalRow_applyStretchResizePolicy(_1, _2, _3, _4, _5) {
            if (_5.portalLayout.preventRowUnderflow) {
                if (_1 && _1.length > 0) {
                    var _6 = _1.map(function (_8) {
                        return isc.isA.Number(_8)
                    }).and();
                    if (_6) {
                        var _7 = _1.sum();
                        if (_7 < _2) {
                            _1[_1.length - 1] = "*"
                        }
                    }
                }
            }
            return this.Super("applyStretchResizePolicy", arguments)
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.defineClass("PortalColumnBody", "Layout");
    isc.A = isc.PortalColumnBody.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.vertical = true;
    isc.A.layoutMargin = 3;
    isc.A.membersMargin = 3;
    isc.A.defaultResizeBars = "none";
    isc.A.canAcceptDrop = true;
    isc.A.canDrag = false;
    isc.A.dropLineThickness = 2;
    isc.A.dropLineProperties = {backgroundColor: "blue"};
    isc.A.width = "100%";
    isc.A.respectSizeLimits = true;
    isc.B.push(isc.A.getTotalMemberSpace = function isc_PortalColumnBody_getTotalMemberSpace() {
            var _1 = this.Super("getTotalMemberSpace", arguments);
            var _2 = this.members.map(function (_3) {
                if (isc.isA.Number(_3.$po)) {
                    return Math.max(_3.$po, _3.minHeight)
                } else {
                    return _3.minHeight
                }
            }).sum();
            return Math.max(_1, _2)
        }
        , isc.A.isPortalColumnDrop = function isc_PortalColumnBody_isPortalColumnDrop() {
            var _1 = this.ns.EH.dragTarget;
            var _2 = _1.getDragType();
            if (_2 == "PortalColumn") return true;
            if (_1.isA("Palette")) {
                var _3 = _1.getDragData(), _4 = (isc.isAn.Array(_3) ? _3[0] : _3);
                if (_4.className == "PortalColumn" || _4.type == "PortalColumn") return true
            }
            return false
        }
        , isc.A.dropMove = function isc_PortalColumnBody_dropMove() {
            if (this.isPortalColumnDrop()) {
                this.hideDropLine()
            } else {
                this.Super("dropMove", arguments);
                return isc.EH.STOP_BUBBLING
            }
        }
        , isc.A.dropOver = function isc_PortalColumnBody_dropOver() {
            if (this.isPortalColumnDrop()) {
                this.hideDropLine()
            } else {
                this.Super("dropOver", arguments);
                return isc.EH.STOP_BUBBLING
            }
        }
        , isc.A.drop = function isc_PortalColumnBody_drop() {
            if (this.isPortalColumnDrop()) {
                this.hideDropLine()
            } else {
                this.Super("drop", arguments);
                return isc.EH.STOP_BUBBLING
            }
        }
        , isc.A.getDropComponent = function isc_PortalColumnBody_getDropComponent(_1, _2) {
            var _3 = this.creator.portalLayout.getDropPortlet(_1, this.creator.portalLayout.getPortalColumnNumber(this.creator), _2, null);
            if (this.handleDroppedEditNode) _3 = this.handleDroppedEditNode(_3, _2);
            if (_3) {
                if (!isc.isA.Portlet(_3)) {
                    _3 = isc.Portlet.create({autoDraw: false, title: "", items: _3, destroyOnClose: true})
                }
                var _4 = _3.portalRow;
                if (_4 && _4.parentElement == this && _4.getMembers().length == 1) {
                    return _4
                } else {
                    this.creator.addPortlet(_3, _2);
                    return null
                }
            }
        }
    );
    isc.B._maxIndex = isc.C + 6;
    isc.A = isc.PortalColumnBody;
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.applyStretchResizePolicy = function isc_c_PortalColumnBody_applyStretchResizePolicy(_1, _2, _3, _4, _5) {
            if (_5.creator.portalLayout.preventColumnUnderflow) {
                if (_1 && _1.length > 0) {
                    var _6 = _1.map(function (_8) {
                        return isc.isA.Number(_8)
                    }).and();
                    if (_6) {
                        var _7 = _1.sum();
                        if (_7 < _2) {
                            _1[_1.length - 1] = "*"
                        }
                    }
                }
            }
            return this.Super("applyStretchResizePolicy", arguments)
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.defineClass("PortalColumn", "Layout");
    isc.A = isc.PortalColumn.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.vertical = true;
    isc.A.minWidth = 80;
    isc.A.dragAppearance = "outline";
    isc.A.canAcceptDrop = false;
    isc.A.canDrop = true;
    isc.A.dragType = "PortalColumn";
    isc.A.showColumnHeader = true;
    isc.A.columnHeaderConstructor = "PortalColumnHeader";
    isc.A.columnHeaderDefaults = {title: "Column"};
    isc.A.rowLayoutDefaults = {_constructor: "PortalColumnBody"};
    isc.A.rowConstructor = "PortalRow";
    isc.B.push(isc.A.setShowColumnHeader = function isc_PortalColumn_setShowColumnHeader(_1) {
            if (_1) {
                if (this.showColumnHeader) return;
                this.showColumnHeader = _1;
                this.addAutoChild("columnHeader", {autoParent: "none"});
                this.addMember(this.columnHeader, 0)
            } else {
                if (!this.showColumnHeader) return;
                this.showColumnHeader = _1;
                this.removeMember(this.columnHeader)
            }
        }
        , isc.A.initWidget = function isc_PortalColumn_initWidget() {
            this.Super("initWidget", arguments);
            this.addAutoChild("columnHeader");
            this.addAutoChild("rowLayout");
            if (this.portalRows) this.addPortalRows(this.portalRows);
            this.portalRows = null
        }
        , isc.A.$87y = function isc_PortalColumn__getDesiredWidth() {
            var _1 = this.getPortalRows();
            if (_1.length == 0) {
                return this.minWidth
            } else {
                var _2 = _1.map(function (_3) {
                    return _3.$87x() + _3.getMarginSpace() + _3.getHMarginBorder()
                }).max();
                _2 += this.$2u() + this.getHMarginBorder() + this.rowLayout.$2u() + this.rowLayout.getHMarginBorder();
                if (this.rowLayout.vscrollOn) _2 += this.rowLayout.getScrollbarSize();
                return Math.max(_2, this.minWidth)
            }
        }
        , isc.A.addNewColumn = function isc_PortalColumn_addNewColumn() {
            this.portalLayout.addColumnAfter(this)
        }
        , isc.A.removeSelf = function isc_PortalColumn_removeSelf() {
            this.portalLayout.removeColumn(this.portalLayout.getMemberNumber(this))
        }
        , isc.A.makePortalRow = function isc_PortalColumn_makePortalRow(_1) {
            if (_1 == null) _1 = {};
            var _2 = {portalLayout: this.portalLayout, portalColumn: this, canResizePortlets: this.canResizePortlets};
            var _3;
            if (isc.isA.PortalRow(_1)) {
                _1.setProperties(_2);
                _3 = _1
            } else {
                isc.addProperties(_1, _2);
                _3 = this.createAutoChild("row", _1)
            }
            return _3
        }
        , isc.A.setCanResizePortlets = function isc_PortalColumn_setCanResizePortlets(_1) {
            this.canResizePortlets = _1;
            this.getPortalRows().map(function (_2) {
                _2.setCanResizePortlets(_1)
            })
        }
        , isc.A.addPortalRows = function isc_PortalColumn_addPortalRows(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _3 = this;
            _1 = _1.map(function (_4) {
                return _3.makePortalRow(_4)
            });
            this.rowLayout.addMembers(_1, _2)
        }
        , isc.A.addPortalRow = function isc_PortalColumn_addPortalRow(_1, _2) {
            this.addPortalRows(_1, _2)
        }
        , isc.A.removePortalRows = function isc_PortalColumn_removePortalRows(_1) {
            this.rowLayout.removeMembers(_1)
        }
        , isc.A.removePortalRow = function isc_PortalColumn_removePortalRow(_1) {
            this.removePortalRows(_1)
        }
        , isc.A.getPortalRows = function isc_PortalColumn_getPortalRows() {
            return this.rowLayout.getMembers()
        }
        , isc.A.getPortalRowNumber = function isc_PortalColumn_getPortalRowNumber(_1) {
            return this.rowLayout.getMemberNumber(_1)
        }
        , isc.A.getPortalRow = function isc_PortalColumn_getPortalRow(_1) {
            return this.rowLayout.getMember(_1)
        }
        , isc.A.getPortlets = function isc_PortalColumn_getPortlets() {
            var _1 = [];
            this.getPortalRows().map(function (_2) {
                _1.addList(_2.getPortlets())
            });
            return _1
        }
        , isc.A.getPortletArray = function isc_PortalColumn_getPortletArray() {
            return this.getPortalRows().map(function (_1) {
                return _1.getPortlets()
            })
        }
        , isc.A.getPortlet = function isc_PortalColumn_getPortlet(_1) {
            var _2 = this.getPortalRows();
            for (var x = 0; x < _2.length; x++) {
                var _4 = _2[x].getPortlet(_1);
                if (_4) return _4
            }
            return null
        }
        , isc.A.addPortlets = function isc_PortalColumn_addPortlets(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _3 = this;
            var _4 = _1.map(function (_5) {
                return _3.makePortalRow({portlets: _5})
            });
            this.addPortalRows(_4, _2)
        }
        , isc.A.addPortlet = function isc_PortalColumn_addPortlet(_1, _2) {
            this.addPortlets(_1, _2)
        }
        , isc.A.addPortletToExistingRow = function isc_PortalColumn_addPortletToExistingRow(_1, _2, _3) {
            var _4 = this.rowLayout.getMembers();
            if (_4 == null || _4.length <= _2) {
                if (this.editContext && this.editNode && _1.editNode) {
                    this.addNode(_1.editNode, this.editNode, _4.length)
                } else {
                    this.addPortlet(_1, _4.length)
                }
            } else {
                var _5 = this.rowLayout.getMember(_2);
                if (_5.editContext && _5.editNode && _1.editNode) {
                    _5.editContext.addNode(_1.editNode, _5.editNode, _3)
                } else {
                    _5.addPortlets(_1, _3)
                }
            }
        }
    );
    isc.B._maxIndex = isc.C + 20;
    isc.defineClass("PortalLayout", "Layout");
    isc.A = isc.PortalLayout.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.vertical = false;
    isc.A.overflow = isc.Canvas.AUTO;
    isc.A.columnOverflow = isc.Canvas.AUTO;
    isc.A.canStretchColumnWidths = true;
    isc.A.canShrinkColumnWidths = true;
    isc.A.preventUnderflow = true;
    isc.A.preventColumnUnderflow = true;
    isc.A.preventRowUnderflow = true;
    isc.A.numColumns = 2;
    isc.A.showColumnMenus = true;
    isc.A.columnBorder = "1px solid gray";
    isc.A.canResizeColumns = false;
    isc.A.canAcceptDrop = true;
    isc.A.dropTypes = ["PortalColumn"];
    isc.A.dropLineThickness = 2;
    isc.A.dropLineProperties = {backgroundColor: "blue"};
    isc.A.rowConstructor = isc.PortalColumn.getInstanceProperty("rowConstructor");
    isc.A.rowLayoutDefaults = isc.PortalColumn.getInstanceProperty("rowLayoutDefaults");
    isc.A.columnConstructor = "PortalColumn";
    isc.B.push(isc.A.setColumnOverflow = function isc_PortalLayout_setColumnOverflow(_1) {
            this.columnOverflow = _1;
            this.rowLayoutDefaults.overflow = _1;
            this.getPortalColumns().map(function (_2) {
                _2.rowLayout.setOverflow(_1)
            })
        }
        , isc.A.setCanStretchColumnWidths = function isc_PortalLayout_setCanStretchColumnWidths(_1) {
            this.canStretchColumnWidths = _1;
            this.reflow("canStretchColumnWidths changed")
        }
        , isc.A.setCanShrinkColumnWidths = function isc_PortalLayout_setCanShrinkColumnWidths(_1) {
            this.canShrinkColumnWidths = _1;
            this.reflow("canShrinkColumnWidths changed")
        }
        , isc.A.setStretchColumnWidthsProportionally = function isc_PortalLayout_setStretchColumnWidthsProportionally(_1) {
            this.stretchColumnWidthsProportionally = _1;
            this.reflow("stretchColumnWidthsProportionally changed")
        }
        , isc.A.setPreventUnderflow = function isc_PortalLayout_setPreventUnderflow(_1) {
            if (this.preventUnderflow == _1) return;
            this.preventUnderflow = _1;
            this.reflow("preventUndeflow changed")
        }
        , isc.A.setPreventColumnUnderflow = function isc_PortalLayout_setPreventColumnUnderflow(_1) {
            if (this.preventColumnUnderflow == _1) return;
            this.preventColumnUnderflow = _1;
            this.getPortalColumns().map(function (_2) {
                _2.rowLayout.reflow("preventColumnUnderflow changed")
            })
        }
        , isc.A.setPreventRowUnderflow = function isc_PortalLayout_setPreventRowUnderflow(_1) {
            if (this.preventRowUnderflow == _1) return;
            this.preventRowUnderflow = _1;
            this.getPortalColumns().map(function (_2) {
                _2.getPortalRows().map(function (_3) {
                    _3.reflow("preventRowUnderflow changed")
                })
            })
        }
        , isc.A.getNumColumns = function isc_PortalLayout_getNumColumns() {
            return this.getMembers().length
        }
        , isc.A.setShowColumnMenus = function isc_PortalLayout_setShowColumnMenus(_1) {
            if (this.showColumnMenus == _1) return;
            this.showColumnMenus = _1;
            this.getPortalColumns().map(function (_2) {
                _2.setShowColumnHeader(_1)
            })
        }
        , isc.A.setColumnBorder = function isc_PortalLayout_setColumnBorder(_1) {
            this.columnBorder = _1;
            var _2 = this.members || [];
            for (var i = 0; i < _2.length; i++) {
                _2[i].setBorder(_1)
            }
        }
        , isc.A.setCanResizeColumns = function isc_PortalLayout_setCanResizeColumns(_1) {
            this.canResizeColumns = _1;
            this.setDefaultResizeBars(_1 ? "all" : "none")
        }
        , isc.A.setCanResizeRows = function isc_PortalLayout_setCanResizeRows(_1) {
            this.setCanResizePortlets(_1)
        }
        , isc.A.setCanResizePortlets = function isc_PortalLayout_setCanResizePortlets(_1) {
            this.canResizePortlets = _1;
            this.getPortalColumns().map(function (_2) {
                _2.setCanResizePortlets(_1)
            })
        }
        , isc.A.initWidget = function isc_PortalLayout_initWidget() {
            this.Super("initWidget", arguments);
            this.setCanResizeColumns(this.canResizeColumns);
            this.setColumnOverflow(this.columnOverflow);
            if (this.canResizeRows != null) this.setCanResizePortlets(this.canResizeRows);
            if (this.portalColumns) {
                this.addPortalColumns(this.portalColumns);
                delete this.portalColumns
            } else if (this.portlets) {
                var _1 = this;
                if (!isc.isAn.Array(this.portlets)) this.portlets = [this.portlets];
                if (!isc.isAn.Array(this.portlets[0])) this.portlets = [this.portlets];
                this.portlets.map(function (_5) {
                    var _2 = _1.makePortalColumn();
                    _1.addPortalColumn(_2);
                    if (!isc.isAn.Array(_5)) _5 = [_5];
                    _5.map(function (_6) {
                        var _3 = _2.makePortalRow();
                        _2.addPortalRow(_3);
                        _3.addPortlets(_6)
                    })
                });
                delete this.portlets
            } else {
                if (this.numColumns) {
                    for (var x = 0; x < this.numColumns; x++) {
                        this.addColumn()
                    }
                }
            }
        }
        , isc.A.getDropPortlet = function isc_PortalLayout_getDropPortlet(_1, _2, _3, _4) {
            return _1
        }
        , isc.A.makePortalColumn = function isc_PortalLayout_makePortalColumn(_1) {
            if (_1 == null) _1 = {};
            var _2 = {
                portalLayout: this,
                showColumnHeader: this.showColumnMenus,
                border: this.columnBorder,
                canResizePortlets: this.canResizePortlets,
                rowConstructor: this.rowConstructor,
                rowDefaults: this.rowDefaults,
                rowProperties: this.rowProperties,
                rowLayoutDefaults: this.rowLayoutDefaults,
                rowLayoutProperties: this.rowLayoutProperties
            };
            var _3;
            if (isc.isA.PortalColumn(_1)) {
                _1.setProperties(_2);
                _3 = _1
            } else {
                isc.addProperties(_1, _2);
                _3 = this.createAutoChild("column", _1)
            }
            return _3
        }
        , isc.A.addMembers = function isc_PortalLayout_addMembers(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _3 = this;
            _1 = _1.map(function (_5) {
                return _3.makePortalColumn(_5)
            });
            this.Super("addMembers", arguments);
            if (this.editContext && !this.$86t) {
                for (var i = 0; i < _1.length; i++) {
                    var _5 = _1[i];
                    if (_5.editNode) {
                        this.editContext.addNode(_5.editNode, this.editNode, _2 + i, null, true)
                    }
                }
            }
        }
        , isc.A.addPortalColumns = function isc_PortalLayout_addPortalColumns(_1, _2) {
            this.$86t = true;
            this.addMembers(_1, _2);
            delete this.$86t
        }
        , isc.A.addPortalColumn = function isc_PortalLayout_addPortalColumn(_1, _2) {
            this.addPortalColumns(_1, _2)
        }
        , isc.A.removeMembers = function isc_PortalLayout_removeMembers(_1) {
            this.Super("removeMembers", arguments);
            if (this.editContext && !this.$86u) {
                if (!isc.isAn.Array(_1)) _1 = [_1];
                var _2 = this;
                _1.map(function (_3) {
                    if (_3.editNode) {
                        _2.editContext.removeNode(_3.editNode, true)
                    }
                })
            }
        }
        , isc.A.removePortalColumns = function isc_PortalLayout_removePortalColumns(_1) {
            this.$86u = true;
            this.removeMembers(_1);
            delete this.$86u
        }
        , isc.A.removePortalColumn = function isc_PortalLayout_removePortalColumn(_1) {
            this.removePortalColumns(_1)
        }
        , isc.A.addColumn = function isc_PortalLayout_addColumn(_1) {
            if (this.editContext) {
                var _2 = this.editContext.makeEditNode({type: this.columnConstructor});
                this.editContext.addNode(_2, this.editNode, _1)
            } else {
                var _3 = "";
                var _4 = 0;
                while (window[(_3 = "PortalColumn" + _4++)]) {
                }
                this.addPortalColumn({ID: _3}, _1)
            }
        }
        , isc.A.removeColumn = function isc_PortalLayout_removeColumn(_1) {
            var _2 = this.members[_1];
            if (_2 != null) {
                if (this.editContext && _2.editNode) {
                    this.editContext.removeNode(_2.editNode)
                } else {
                    _2.destroy()
                }
            }
        }
        , isc.A.addColumnAfter = function isc_PortalLayout_addColumnAfter(_1) {
            var _2 = this.getMemberNumber(_1) + 1;
            this.addColumn(_2)
        }
        , isc.A.getPortlets = function isc_PortalLayout_getPortlets() {
            var _1 = [];
            this.getPortalColumns().map(function (_2) {
                _1.addList(_2.getPortlets())
            });
            return _1
        }
        , isc.A.getPortletArray = function isc_PortalLayout_getPortletArray() {
            return this.getPortalColumns().map(function (_1) {
                return _1.getPortletArray()
            })
        }
        , isc.A.addPortlet = function isc_PortalLayout_addPortlet(_1, _2, _3, _4) {
            if (_3 == null) _3 = 0;
            if (_2 == null) _2 = 0;
            var _5 = this.getMember(_2);
            if (_5 != null) {
                if (_4 == null) {
                    if (_5.editContext && _5.editNode && _1.editNode) {
                        _5.editContext.addNode(_1.editNode, _5.editNode, _3)
                    } else {
                        _5.addPortlet(_1, _3)
                    }
                } else {
                    _5.addPortletToExistingRow(_1, _3, _4)
                }
            }
        }
        , isc.A.getTotalMemberSpace = function isc_PortalLayout_getTotalMemberSpace() {
            var _1 = this.Super("getTotalMemberSpace", arguments);
            var _2 = this.members.map(function (_3) {
                if (isc.isA.Number(_3.$pn)) {
                    return Math.max(_3.$pn, _3.minWidth)
                } else {
                    return _3.minWidth
                }
            }).sum();
            return Math.max(_1, _2)
        }
        , isc.A.setColumnWidth = function isc_PortalLayout_setColumnWidth(_1, _2) {
            var _3 = this.getPortalColumn(_1);
            if (!_3) return;
            if (_3.editContext && _3.editNode) {
                _3.editContext.setNodeProperties(_3.editNode, {width: _2})
            } else {
                _3.setWidth(_2)
            }
        }
        , isc.A.getColumnWidth = function isc_PortalLayout_getColumnWidth(_1) {
            var _2 = this.getPortalColumn(_1);
            if (_2) {
                return _2.getWidth()
            } else {
                return null
            }
        }
        , isc.A.getPortalColumns = function isc_PortalLayout_getPortalColumns() {
            return this.getMembers()
        }
        , isc.A.getPortalColumn = function isc_PortalLayout_getPortalColumn(_1) {
            return this.getMember(_1)
        }
        , isc.A.getPortalColumnNumber = function isc_PortalLayout_getPortalColumnNumber(_1) {
            return this.getMemberNumber(_1)
        }
        , isc.A.getColumn = function isc_PortalLayout_getColumn(_1) {
            return this.getPortalColumn(_1)
        }
        , isc.A.removePortlet = function isc_PortalLayout_removePortlet(_1) {
            if (this.editContext && _1.editNode) {
                this.editContext.removeNode(_1.editNode)
            } else {
                if (_1.portalRow) _1.portalRow.removePortlets(_1)
            }
        }
    );
    isc.B._maxIndex = isc.C + 36;
    isc.A = isc.PortalLayout;
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.applyStretchResizePolicy = function isc_c_PortalLayout_applyStretchResizePolicy(_1, _2, _3, _4, _5) {
            if (_5.preventUnderflow) {
                if (_1 && _1.length > 0) {
                    var _6 = _1.map(function (_16) {
                        return isc.isA.Number(_16)
                    }).and();
                    if (_6) {
                        var _7 = _1.sum();
                        if (_7 < _2) {
                            _1[_1.length - 1] = "*"
                        }
                    }
                }
            }
            var _8 = this.Super("applyStretchResizePolicy", arguments);
            if (_4) _8 = _1;
            var _9 = _5.getPortalColumns().map("$87y");
            var _10 = 0;
            if (_5.canStretchColumnWidths) {
                if (_5.stretchColumnWidthsProportionally) {
                    var _11 = 1;
                    for (var i = 0; i < _8.length; i++) {
                        var _13 = (_9[i] / _8[i]);
                        _11 = Math.max(_11, _13)
                    }
                    if (_11 > 1) {
                        for (var i = 0; i < _8.length; i++) {
                            _8[i] = _8[i] * _11
                        }
                    }
                } else {
                    for (var i = 0; i < _8.length; i++) {
                        if (_9[i] > _8[i]) {
                            _10 += _9[i] - _8[i];
                            _8[i] = _9[i]
                        } else if (_10 && _5.canShrinkColumnWidths) {
                            var _14 = _8[i] - _9[i];
                            var _15 = Math.min(_10, _14);
                            _8[i] -= _15;
                            _10 -= _15
                        }
                    }
                    if (_10 && _5.canShrinkColumnWidths) {
                        for (var i = 0; i < _8.length; i++) {
                            if (_9[i] < _8[i]) {
                                var _14 = _8[i] - _9[i];
                                var _15 = Math.min(_10, _14);
                                _8[i] -= _15;
                                _10 -= _15;
                                if (_10 == 0) break
                            }
                        }
                    }
                }
            }
            return _8
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.ClassFactory.defineClass("Dialog", "Window");
    isc.A = isc.Dialog;
    isc.A.$76 = [];
    isc.A.OK_BUTTON_TITLE = "OK";
    isc.A.APPLY_BUTTON_TITLE = "Apply";
    isc.A.YES_BUTTON_TITLE = "Yes";
    isc.A.NO_BUTTON_TITLE = "No";
    isc.A.CANCEL_BUTTON_TITLE = "Cancel";
    isc.A.DONE_BUTTON_TITLE = "Done";
    isc.A.CONFIRM_TITLE = "Confirm";
    isc.A.SAY_TITLE = "Note";
    isc.A.WARN_TITLE = "Warning";
    isc.A.ASK_TITLE = "Question";
    isc.A.ASK_FOR_VALUE_TITLE = "Please enter a value";
    isc.A.LOGIN_TITLE = "Please log in";
    isc.A.USERNAME_TITLE = "Username";
    isc.A.PASSWORD_TITLE = "Password";
    isc.A.LOGIN_BUTTON_TITLE = "Log in";
    isc.A.LOGIN_ERROR_MESSAGE = "Invalid username or password";
    isc.A.OK = {
        getTitle: function () {
            return isc.Dialog.OK_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.okClick()
        }
    };
    isc.A.APPLY = {
        getTitle: function () {
            return isc.Dialog.APPLY_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.applyClick()
        }
    };
    isc.A.YES = {
        getTitle: function () {
            return isc.Dialog.YES_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.yesClick()
        }
    };
    isc.A.NO = {
        getTitle: function () {
            return isc.Dialog.NO_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.noClick()
        }
    };
    isc.A.CANCEL = {
        getTitle: function () {
            return isc.Dialog.CANCEL_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.cancelClick()
        }
    };
    isc.A.DONE = {
        getTitle: function () {
            return isc.Dialog.DONE_BUTTON_TITLE
        }, width: 75, click: function () {
            this.topElement.doneClick()
        }
    };
    isc.A = isc.Dialog.getPrototype();
    isc.A.defaultWidth = 360;
    isc.A.title = "Dialog";
    isc.A.styleName = "dialogBackground";
    isc.A.skinImgDir = "images/Dialog/";
    isc.A.canDragReposition = false;
    isc.A.canDragResize = false;
    isc.A.autoCenter = true;
    isc.A.bodyStyle = "dialogBody";
    isc.A.bodyColor = "#DDDDDD";
    isc.A.hiliteBodyColor = "#FFFFFF";
    isc.A.bodyDefaults = isc.addProperties({}, isc.Window.getInstanceProperty("bodyDefaults"), {
        layoutTopMargin: 15,
        layoutLeftMargin: 15,
        layoutRightMargin: 15,
        layoutBottomMargin: 5
    });
    isc.A.messageStyle = "normal";
    isc.A.messageLabelDefaults = {width: "100%", canSelectText: true};
    isc.A.messageIconDefaults = {width: 32, height: 32};
    isc.A.messageStackDefaults = {height: 1, layoutMargin: 10, layoutBottomMargin: 5, membersMargin: 10};
    isc.A.autoChildParentMap = isc.addProperties({}, isc.Window.getInstanceProperty("autoChildParentMap"), {
        messageStack: "body",
        messageIcon: "messageStack",
        messageLabel: "messageStack"
    });
    isc.A.headerStyle = "dialogHeader";
    isc.A.hiliteHeaderStyle = "dialogHeaderHilite";
    isc.A.headerLabelDefaults = isc.addProperties({}, isc.Window.getInstanceProperty("headerLabelDefaults"), {styleName: "dialogHeaderText"});
    isc.A.showHeaderIcon = false;
    isc.A.showMinimizeButton = false;
    isc.A.showMaximizeButton = false;
    isc.A.showFooter = false;
    isc.A.showToolbar = true;
    isc.A.autoFocus = true;
    isc.A.askIcon = "[SKIN]ask.png";
    isc.A.sayIcon = "[SKIN]say.png";
    isc.A.warnIcon = "[SKIN]warn.png";
    isc.A.confirmIcon = "[SKIN]confirm.png";
    isc.A.notifyIcon = "[SKIN]notify.png";
    isc.A.errorIcon = "[SKIN]error.png";
    isc.A.stopIcon = "[SKIN]stop.png";
    isc.A.toolbarDefaults = isc.addProperties({}, isc.Window.getInstanceProperty("toolbarDefaults"), {
        layoutAlign: "center",
        width: 20,
        click: function (_1, _2) {
            this.Super("click", arguments);
            var _3 = isc.EH.getTarget(), _4 = this.getMemberNumber(_3);
            if (_4 == -1) return;
            this.topElement.buttonClick(_3, _4)
        }
    });
    isc.A = isc.Dialog.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.namedLocatorChildren = ["okButton", "applyButton", "yesButton", "noButton", "cancelButton", "doneButton"];
    isc.B.push(isc.A.initWidget = function isc_Dialog_initWidget() {
            if (this.message != null) {
                this.autoSize = true
            }
            this.Super("initWidget", arguments);
            if (this.buttons) {
                this.toolbarButtons = this.buttons
            }
        }
        , isc.A.createChildren = function isc_Dialog_createChildren() {
            var _1 = this.showToolbar;
            this.showToolbar = false;
            this.Super("createChildren");
            this.showToolbar = _1;
            if (this.message != null) {
                this.body.hPolicy = "fill";
                this.addAutoChild("messageStack", null, isc.HStack);
                if (this.icon != null) {
                    this.addAutoChild("messageIcon", {src: this.getImgURL(this.icon)}, isc.Img)
                }
                var _2 = this.message.evalDynamicString(this, {loadingImage: this.imgHTML(isc.Canvas.loadingImageSrc, isc.Canvas.loadingImageSize, isc.Canvas.loadingImageSize)});
                this.addAutoChild("messageLabel", {contents: _2, baseStyle: this.messageStyle}, isc.Label)
            }
            if (this.showToolbar) {
                this.makeToolbar()
            }
        }
        , isc.A.draw = function isc_Dialog_draw() {
            if (!this.readyToDraw()) return this;
            this.Super("draw", arguments);
            if (this.toolbar && this.autoFocus) {
                var _1 = this.toolbar.getMember(0);
                if (_1) _1.focus()
            }
            return this
        }
        , isc.A.saveData = function isc_Dialog_saveData() {
        }
        , isc.A.cancelClick = function isc_Dialog_cancelClick() {
            return this.closeClick()
        }
        , isc.A.$7e = function isc_Dialog__closeButtonClick() {
            return this.cancelClick()
        }
        , isc.A.okClick = function isc_Dialog_okClick() {
            this.saveData();
            this.clear();
            this.returnValue(true)
        }
        , isc.A.applyClick = function isc_Dialog_applyClick() {
            this.saveData()
        }
        , isc.A.yesClick = function isc_Dialog_yesClick() {
            this.returnValue(true)
        }
        , isc.A.noClick = function isc_Dialog_noClick() {
            this.returnValue(false)
        }
        , isc.A.doneClick = function isc_Dialog_doneClick() {
            this.clear();
            this.returnValue(true)
        }
        , isc.A.buttonClick = function isc_Dialog_buttonClick(_1, _2) {
        }
    );
    isc.B._maxIndex = isc.C + 12;
    isc.Dialog.changeDefaults("toolbarDefaults", {
        makeButton: function (_1) {
            var _2 = _1, _1 = this.Super("makeButton", arguments);
            switch (_2) {
                case isc.Dialog.OK:
                    this.creator.okButton = _1;
                    _1.locatorParent = this.creator;
                    break;
                case isc.Dialog.APPLY:
                    this.creator.applyButton = _1;
                    _1.locatorParent = this.creator;
                    break;
                case isc.Dialog.YES:
                    this.creator.yesButton = _1;
                    _1.locatorParent = this.creator;
                    break;
                case isc.Dialog.NO:
                    this.creator.noButton = _1;
                    _1.locatorParent = this.creator;
                    break;
                case isc.Dialog.CANCEL:
                    this.creator.cancelButton = _1;
                    _1.locatorParent = this.creator;
                    break;
                case isc.Dialog.DONE:
                    this.creator.doneButton = _1;
                    _1.locatorParent = this.creator;
                    break
            }
            return _1
        }
    });
    isc.Dialog.Prompt = {
        ID: "isc_globalPrompt",
        _generated: true,
        width: 400,
        height: 90,
        autoDraw: false,
        autoSize: true,
        isModal: true,
        showHeader: false,
        showFooter: false,
        showToolbar: false,
        dismissOnEscape: false,
        bodyStyle: "promptBody",
        bodyDefaults: isc.addProperties({}, isc.Dialog.getInstanceProperty("bodyDefaults"), {height: "100%"}),
        message: "Loading...&nbsp;${loadingImage}",
        messageStackDefaults: isc.addProperties({}, isc.Dialog.getInstanceProperty("messageStackDefaults"), {
            height: "100%",
            width: "100%",
            layoutAlign: "center"
        }),
        messageLabelDefaults: isc.addProperties({}, isc.Dialog.getInstanceProperty("messageLabelDefaults"), {
            width: "100%",
            align: isc.Canvas.CENTER,
            valign: isc.Canvas.CENTER
        }),
        layoutMargin: 0,
        showMessage: function (_1, _2) {
            this.setProperties(_2);
            if (_1 == null) _1 = "&nbsp;";
            this.message = _1.evalDynamicString(this, {loadingImage: this.imgHTML(isc.Canvas.loadingImageSrc, isc.Canvas.loadingImageSize, isc.Canvas.loadingImageSize)});
            if (!this.$7f) this.createChildren();
            this.messageLabel.setContents(this.message == null ? "&nbsp;" : this.message);
            this.show()
        },
        clearMessage: function () {
            if (this.pendingFade) {
                isc.Timer.clearTimeout(this.pendingFade);
                delete this.pendingFade
            }
            if (this.isAnimating(this.$zb)) {
                this.finishAnimation(this.$zb)
            }
            this.clear();
            if (this.$89e) {
                this.fireCallback(this.$89e);
                delete this.$89e
            }
        },
        fadeDuration: 2000,
        fadeMessage: function () {
            delete this.pendingFade;
            this.animateHide("fade", {target: this, methodName: "clearMessage"})
        },
        destroy: function () {
            isc.Dialog.Prompt = this.$77;
            return this.Super("destroy", arguments)
        }
    };
    isc.addGlobal("showPrompt", function (_1, _2) {
        var _3 = isc.Dialog.Prompt;
        if (!isc.isA.Dialog(_3)) {
            var _4 = _3;
            _3 = isc.Dialog.Prompt = isc.Dialog.create(_3);
            _3.$77 = _4
        }
        isc.Dialog.Prompt.showMessage(_1, _2)
    });
    isc.addGlobal("clearPrompt", function () {
        if (!isc.isA.Dialog(isc.Dialog.Prompt)) return;
        isc.Dialog.Prompt.clearMessage()
    });
    isc.addGlobal("showFadingPrompt", function (_1, _2, _3, _4) {
        if (isc.isA.Canvas(isc.Dialog.Prompt) && isc.Dialog.Prompt.isDrawn()) {
            isc.clearPrompt()
        }
        isc.showPrompt(_1, _4);
        var _5 = isc.Dialog.Prompt;
        if (_2 == null) _2 = _5.fadeDuration;
        _5.$89e = _3;
        _5.pendingFade = _5.delayCall("fadeMessage", [], _2)
    });
    isc.Dialog.Warn = {
        ID: "isc_globalWarn",
        _generated: true,
        width: 360,
        height: 60,
        isModal: true,
        canDragReposition: true,
        keepInParentRect: true,
        autoDraw: false,
        autoSize: true,
        autoCenter: true,
        buttons: [isc.Dialog.OK],
        message: "Your message here!",
        showMessage: function (_1, _2) {
            if (_1 == null) _1 = "&nbsp;";
            this.message = _1;
            if (!this.icon && _2.icon) this.icon = _2.icon;
            this.setProperties(_2);
            if (_2.callback == null) delete this.callback;
            if (!this.$7f) this.createChildren();
            this.messageLabel.setContents(this.message == null ? "&nbsp;" : this.message);
            if (this.icon) {
                if (this.messageIcon) {
                    this.messageIcon.setSrc(this.getImgURL(this.icon));
                    this.messageIcon.show()
                }
            } else this.messageIcon.hide();
            this.toolbar.layoutChildren();
            if (this.messageLabel.isDirty()) this.messageLabel.redraw();
            if (this.isDrawn()) {
                this.messageStack.layoutChildren();
                this.body.layoutChildren();
                this.layoutChildren()
            }
            this.show();
            if (this.toolbar) {
                var _3 = this.toolbar.getMember(0);
                _3.focus()
            }
        }
    };
    isc.addGlobal("showMessage", function (_1, _2, _3, _4) {
        if ((isc.isA.String(_4) || isc.isA.Function(_4)) || (_4 == null && isc.isAn.Object(_3) && _3.methodName == null && _3.action == null && _3.method == null)) {
            var _5 = _4;
            _4 = _3;
            _3 = _5
        }
        if (!isc.isA.Dialog(isc.Dialog.Warn) || (isc.isA.Function(isc.Dialog.Warn.initialized) && !isc.Dialog.Warn.initialized())) {
            isc.Dialog.Warn = isc.Dialog.create(isc.Dialog.Warn)
        }
        if (!_4) _4 = {};
        if (_4.toolbarButtons != null) {
            _4.buttons = _4.toolbarButtons;
            delete _4.toolbarButtons
        }
        if (!_4.buttons) {
            if (_2 == "confirm") {
                _4.buttons = [isc.Dialog.OK, isc.Dialog.CANCEL]
            } else if (_2 == "ask") {
                _4.buttons = [isc.Dialog.YES, isc.Dialog.NO]
            } else {
                _4.buttons = [isc.Dialog.OK]
            }
        }
        if (!_4.title) {
            if (_2 == "confirm") _4.title = isc.Dialog.CONFIRM_TITLE; else if (_2 == "ask") _4.title = isc.Dialog.ASK_TITLE; else if (_2 == "warn") _4.title = isc.Dialog.WARN_TITLE; else _4.title = isc.Dialog.SAY_TITLE
        }
        isc.$78(_4);
        if (!_4.icon) _4.icon = isc.Dialog.getInstanceProperty(_2 + "Icon");
        if (_3) _4.callback = _3;
        isc.Dialog.Warn.showMessage(_1, _4)
    });
    isc.addGlobal("getLastDialog", function () {
        return isc.Dialog.Warn
    });
    isc.addGlobal("dismissLastDialog", function () {
        if (isc.Dialog.Warn) isc.Dialog.Warn.hide()
    });
    isc.$78 = function (_1) {
        var _2 = this.$79 = this.$79 || ["okClick", "yesClick", "noClick", "cancelClick", "closeClick", "applyClick"];
        for (var i = 0; i < _2.length; i++) {
            var _4 = _2[i];
            if (!_1[_4]) {
                _1[_4] = isc.Dialog.getInstanceProperty(_4)
            }
        }
    };
    isc.addGlobal("warn", function (_1, _2, _3) {
        isc.showMessage(_1, "warn", _2, _3)
    });
    isc.addGlobal("say", function (_1, _2, _3) {
        isc.showMessage(_1, "say", _2, _3)
    });
    isc.addGlobal("ask", function (_1, _2, _3) {
        isc.showMessage(_1, "ask", _2, _3)
    });
    isc.confirm = function (_1, _2, _3) {
        isc.showMessage(_1, "confirm", _2, _3)
    };
    isc.askForValue = function (_1, _2, _3) {
        _3 = _3 || isc.emptyObject;
        var _4 = isc.Dialog.Ask;
        if (!_4) {
            var _5 = isc.DynamicForm.create({
                numCols: 1,
                padding: 3,
                items: [{name: "message", type: "blurb"}, {name: "value", showTitle: false, width: "*"}],
                saveOnEnter: true,
                submit: function () {
                    this.askDialog.okClick()
                }
            });
            _4 = isc.Dialog.Ask = isc.Dialog.create({
                items: [_5],
                askForm: _5,
                canDragReposition: true,
                isModal: true,
                bodyProperties: {overflow: "visible"},
                overflow: "visible"
            });
            _5.askDialog = _4;
            _4.$8a = function () {
                this.clear();
                this.returnValue(this.askForm.getValue("value"))
            }
        }
        var _6 = _3.left != null || _3.top != null;
        if (_3.toolbarButtons != null) {
            _3.buttons = _3.toolbarButtons;
            delete _3.toolbarButtons
        }
        _3 = isc.addProperties({
            callback: _2,
            title: _3.title || isc.Dialog.ASK_FOR_VALUE_TITLE,
            autoCenter: !_6,
            left: (_6 ? _3.left || "10%" : null),
            top: (_6 ? _3.top || "20%" : null),
            width: _3.width || "80%",
            height: _3.height || 20,
            buttons: _3.buttons || [isc.Dialog.OK, isc.Dialog.CANCEL],
            okClick: _3.okClick || _4.$8a
        }, _3);
        isc.$78(_3);
        _4.setProperties(_3);
        _4.askForm.setValues({message: _1 || "Please enter a value:", value: _3.defaultValue || ""});
        _4.show();
        _4.askForm.focusInItem("value")
    };
    isc.ClassFactory.defineClass("LoginDialog", "Window");
    isc.LoginDialog.registerStringMethods({register: "values, form", lostPassword: "values, form"});
    isc.A = isc.LoginDialog;
    isc.A.firstTimeInit = true;
    isc.A = isc.LoginDialog.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.dismissable = false;
    isc.A.allowBlankPassword = false;
    isc.A.showLostPasswordLink = false;
    isc.A.showRegistrationLink = false;
    isc.A.errorStyle = "formCellError";
    isc.A.lostPasswordItemTitle = "Lost Password?";
    isc.A.registrationItemTitle = "Register";
    isc.A.autoCenter = true;
    isc.A.autoSize = true;
    isc.A.isModal = true;
    isc.A.showMinimizeButton = false;
    isc.A.items = ["autoChild:loginForm"];
    isc.A.loginFormConstructor = "DynamicForm";
    isc.A.loginFormDefaults = {
        numCols: 2, padding: 4, autoDraw: false, saveOnEnter: true, submit: function () {
            var _1 = this, _2 = [{username: this.getValue("usernameItem"), password: this.getValue("passwordItem")}];
            _2[1] = function (_3, _4) {
                if (_3) {
                    _1.complete()
                } else {
                    if (_4 != null)
                        _1.setValue("loginFailureItem", _4);
                    _1.showItem("loginFailureItem");
                    _1.focusInItem("passwordItem")
                }
            };
            this.fireCallback(this.loginDialog.loginFunc, "credentials,dialogCallback", _2)
        }, complete: function (_1) {
            this.loginDialog.hide();
            this.setValue("loginFailureItem", this.loginDialog.errorMessage);
            this.setValue("usernameItem", "");
            this.setValue("passwordItem", "");
            this.hideItem("loginFailureItem");
            if (_1) {
                this.fireCallback(this.loginFunc, "credentials,dialogCallback")
            } else {
                var _2 = isc.Cookie.get("loginRedirect");
                if (_2) window.location.replace(_2)
            }
        }
    };
    isc.A.formDSDefaults = {clientOnly: true, useAllDataSourceFields: true};
    isc.A.formDefaultFields = [{
        name: "loginFailureItem",
        type: "blurb",
        colSpan: 2,
        visible: false
    }, {
        name: "usernameItem",
        required: true,
        browserSpellCheck: false,
        browserAutoCorrect: false,
        browserAutoCapitalize: false,
        keyPress: function (_1, _2, _3) {
            if (_3 == "Enter") {
                _2.focusInItem("passwordItem");
                return false
            }
        }
    }, {name: "passwordItem", type: "password", required: true}, {
        name: "loginButton",
        type: "button",
        type: "submit"
    }, {
        name: "lostPasswordItem",
        type: "link",
        target: "javascript",
        canEdit: false,
        endRow: true,
        numCols: 2,
        colSpan: 2,
        showTitle: false,
        click: "form.loginDialog.lostPassword(form.getValues(), form)"
    }, {
        name: "registrationItem",
        type: "link",
        target: "javascript",
        canEdit: false,
        endRow: true,
        numCols: 2,
        colSpan: 2,
        showTitle: false,
        click: "form.loginDialog.register(form.getValues(), form)"
    }];
    isc.B.push(isc.A.getDynamicDefaults = function isc_LoginDialog_getDynamicDefaults(_1) {
            switch (_1) {
                case"loginForm":
                    var _2 = {
                        loginDialog: this,
                        values: {
                            usernameItem: this.username || "",
                            passwordItem: this.password || "",
                            loginFailureItem: this.errorMessage
                        },
                        fields: this.formFields
                    };
                    var _3 = isc.clone(this.formDefaultFields);
                    for (var j = 0; j < _3.length; j++) {
                        var _5 = _3[j], _6 = _5.name;
                        isc.addProperties(_5, this[_6 + "Defaults"], this[_6 + "Properties"]);
                        if (null != this[_6 + "Title"]) {
                            _5.title = this[_6 + "Title"];
                            if (_5.type == 'link' && !_5.showTitle)
                                _5.linkTitle = this[_6 + "Title"]
                        }
                        var _7 = this["show" + _6.substr(0, 1).toUpperCase() + _6.substr(1)];
                        if (null != _7) _5.visible = _7;
                        switch (_6) {
                            case"registrationItem":
                                _5.visible = this.showRegistrationLink;
                                break;
                            case"lostPasswordItem":
                                _5.visible = this.showLostPasswordLink;
                                break;
                            case"loginFailureItem":
                                _5.cellStyle = this.errorStyle;
                                break;
                            case"passwordItem":
                                _5.required = !this.allowBlankPassword;
                                break
                        }
                        _3[j] = _5
                    }
                    _2.dataSource = isc.DataSource.create(this.formDSDefaults, {fields: _3});
                    return _2
            }
            return null
        }
        , isc.A.cancelClick = function isc_LoginDialog_cancelClick() {
            this.loginForm.complete(true)
        }
        , isc.A.init = function isc_LoginDialog_init() {
            if (isc.LoginDialog.firstTimeInit) {
                isc.LoginDialog.firstTimeInit = false;
                isc.LoginDialog.addProperties({
                    title: isc.Dialog.LOGIN_TITLE,
                    usernameItemTitle: isc.Dialog.USERNAME_TITLE,
                    passwordItemTitle: isc.Dialog.PASSWORD_TITLE,
                    loginButtonTitle: isc.Dialog.LOGIN_BUTTON_TITLE,
                    errorMessage: isc.Dialog.LOGIN_ERROR_MESSAGE
                })
            }
            this.dismissOnEscape = this.showCloseButton = this.dismissable;
            this.Super("init", arguments);
            this.loginForm.focusInItem("usernameItem");
            if (this.username) this.loginForm.setValue("usernameItem", this.username);
            if (this.password) this.loginForm.setValue("passwordItem", this.password)
        }
    );
    isc.B._maxIndex = isc.C + 3;
    isc.showLoginDialog = function (_1, _2) {
        return isc.LoginDialog.create(isc.addProperties({}, _2, {autoDraw: true, loginFunc: _1}))
    };
    isc.defineClass("MultiSortPanel", "Layout");
    isc.A = isc.MultiSortPanel.getPrototype();
    isc.A.vertical = true;
    isc.A.overflow = "visible";
    isc.A.addLevelButtonTitle = "Add Level";
    isc.A.deleteLevelButtonTitle = "Delete Level";
    isc.A.copyLevelButtonTitle = "Copy Level";
    isc.A.invalidListPrompt = "Columns may only be used once: '\${title}' is used multiple times.";
    isc.A.propertyFieldTitle = "Column";
    isc.A.directionFieldTitle = "Order";
    isc.A.ascendingTitle = "Ascending";
    isc.A.descendingTitle = "Descending";
    isc.A.firstSortLevelTitle = "Sort by";
    isc.A.otherSortLevelTitle = "Then by";
    isc.A.topLayoutDefaults = {
        _constructor: "HLayout",
        overflow: "visible",
        height: 22,
        align: "left",
        membersMargin: 5,
        extraSpace: 5
    };
    isc.A.addLevelButtonDefaults = {
        _constructor: "IButton",
        icon: "[SKINIMG]actions/add.png",
        autoFit: true,
        height: 22,
        showDisabled: false,
        autoParent: "topLayout",
        click: "this.creator.addLevel()"
    };
    isc.A.deleteLevelButtonDefaults = {
        _constructor: "IButton",
        icon: "[SKINIMG]actions/remove.png",
        autoFit: true,
        height: 22,
        showDisabled: false,
        autoParent: "topLayout",
        click: "this.creator.deleteSelectedLevel()"
    };
    isc.A.copyLevelButtonDefaults = {
        _constructor: "IButton",
        icon: "[SKINIMG]RichTextEditor/copy.png",
        autoFit: true,
        height: 22,
        showDisabled: false,
        autoParent: "topLayout",
        click: "this.creator.copySelectedLevel()"
    };
    isc.A.levelUpButtonTitle = "Move Level Up";
    isc.A.levelUpButtonDefaults = {
        _constructor: "ImgButton",
        src: "[SKINIMG]common/arrow_up.gif",
        height: 22,
        width: 20,
        imageType: "center",
        showDisabled: false,
        showRollOver: false,
        showDown: false,
        showFocused: false,
        autoParent: "topLayout",
        click: "this.creator.moveSelectedLevelUp()"
    };
    isc.A.levelDownButtonTitle = "Move Level Down";
    isc.A.levelDownButtonDefaults = {
        _constructor: "ImgButton",
        src: "[SKINIMG]common/arrow_down.gif",
        height: 22,
        width: 20,
        imageType: "center",
        showDisabled: false,
        showRollOver: false,
        showDown: false,
        showFocused: false,
        autoParent: "topLayout",
        click: "this.creator.moveSelectedLevelDown()"
    };
    isc.A.optionsGridDefaults = {
        _constructor: "ListGrid",
        width: "100%",
        height: "*",
        canSort: false,
        canReorderFields: false,
        canResizeFields: false,
        canEdit: true,
        canEditNew: true,
        selectionType: "single",
        selectionProperty: "$73s",
        fields: [{
            name: "sortSequence",
            title: "&nbsp;",
            showTitle: false,
            canEdit: false,
            width: 80,
            canHide: false,
            showHeaderContextMenuButton: false,
            formatCellValue: function (_1, _2, _3, _4, _5) {
                return _3 == 0 ? _5.creator.firstSortLevelTitle : _5.creator.otherSortLevelTitle
            }
        }, {
            name: "property",
            title: " ",
            type: "select",
            defaultToFirstOption: true,
            showHeaderContextMenuButton: false,
            changed: "item.grid.creator.fireChangeEvent()"
        }, {
            name: "direction",
            title: " ",
            type: "select",
            showHeaderContextMenuButton: false,
            defaultToFirstOption: true,
            changed: "item.grid.creator.fireChangeEvent()"
        }],
        recordClick: function (_1, _2, _3) {
            this.creator.setButtonStates()
        },
        bodyKeyPress: function (_1) {
            if (_1.keyName == "Delete" && this.anySelected()) this.removeSelectedData(); else this.Super("bodyKeyPress", arguments)
        },
        extraSpace: 5
    };
    isc.A.propertyFieldNum = 1;
    isc.A.directionFieldNum = 2;
    isc.A.topAutoChildren = ["topLayout", "addLevelButton", "deleteLevelButton", "copyLevelButton"];
    isc.A = isc.MultiSortPanel.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.getNumLevels = function isc_MultiSortPanel_getNumLevels() {
            return this.optionsGrid.data.length
        }
        , isc.A.getSortLevel = function isc_MultiSortPanel_getSortLevel(_1) {
            return this.getSortSpecifier(this.data.get(_1))
        }
        , isc.A.getSort = function isc_MultiSortPanel_getSort() {
            var _1 = this.optionsGrid, _2 = _1.data.duplicate(), _3 = _1.getEditRow(),
                _4 = isc.isA.Number(_3) ? _1.getEditValues(_3) : null;
            if (_4) _2[_3] = isc.addProperties(_2[_3], _4);
            return this.getSortSpecifiers(_2)
        }
        , isc.A.setSort = function isc_MultiSortPanel_setSort(_1) {
            this.optionsGrid.setData(_1)
        }
        , isc.A.validate = function isc_MultiSortPanel_validate() {
            var _1 = this.optionsGrid, _2 = _1.data, _3 = [];
            for (var i = 0; i < _2.length; i++) {
                var _5 = _2.get(i);
                if (_3.contains(_5.property)) {
                    var _6 = this, _7 = this.optionsGrid.getField("property").valueMap[_5.property],
                        _8 = this.invalidListPrompt.evalDynamicString(this, {title: _7});
                    isc.warn(_8, function () {
                        _6.recordFailedValidation(_5, i)
                    });
                    return false
                }
                _3.add(_5.property)
            }
            return true
        }
        , isc.A.recordFailedValidation = function isc_MultiSortPanel_recordFailedValidation(_1) {
            var _2 = this.optionsGrid, _3 = (isc.isA.Number(_1) ? _1 : _2.getRecordIndex(_1)),
                _1 = (!isc.isA.Number(_1) ? _1 : _2.data.get(_1));
            _2.selectSingleRecord(_1);
            _2.startEditing(_3, 1)
        }
        , isc.A.getSortSpecifier = function isc_MultiSortPanel_getSortSpecifier(_1) {
            if (isc.isA.Number(_1)) _1 = this.optionsGrid.data.get(_1);
            return this.optionsGrid.removeSelectionMarkers(_1)
        }
        , isc.A.getSortSpecifiers = function isc_MultiSortPanel_getSortSpecifiers(_1) {
            return this.optionsGrid.removeSelectionMarkers(_1)
        }
        , isc.A.setSortSpecifiers = function isc_MultiSortPanel_setSortSpecifiers(_1) {
            this.optionsGrid.setData(_1)
        }
        , isc.A.initWidget = function isc_MultiSortPanel_initWidget() {
            this.Super("initWidget", arguments);
            this.$74x = this.maxLevels;
            this.addAutoChildren(this.topAutoChildren);
            this.addAutoChild("levelUpButton", {prompt: this.levelUpButtonTitle});
            this.addAutoChild("levelDownButton", {prompt: this.levelDownButtonTitle});
            this.addAutoChild("optionsGrid");
            this.setSortFields();
            this.setSortDirections();
            this.setButtonTitles();
            this.addMember(this.topLayout);
            this.addMember(this.optionsGrid);
            this.setButtonStates();
            if (this.initialSort) this.setSortSpecifiers(this.initialSort); else this.addLevel()
        }
        , isc.A.setButtonTitles = function isc_MultiSortPanel_setButtonTitles(_1) {
            if (this.addLevelButton) this.addLevelButton.setTitle(this.addLevelButtonTitle);
            if (this.deleteLevelButton) this.deleteLevelButton.setTitle(this.deleteLevelButtonTitle);
            if (this.copyLevelButton) this.copyLevelButton.setTitle(this.copyLevelButtonTitle)
        }
        , isc.A.setButtonStates = function isc_MultiSortPanel_setButtonStates() {
            var _1 = this.getNumLevels(), _2 = this.maxLevels, _3 = this.optionsGrid, _4 = _3.anySelected(),
                _5 = _3.getRecordIndex(_3.getSelectedRecord());
            if (this.addLevelButton) this.addLevelButton.setDisabled(_1 >= _2);
            if (this.deleteLevelButton) this.deleteLevelButton.setDisabled(!_4);
            if (this.copyLevelButton) this.copyLevelButton.setDisabled(!_4 || _1 >= _2);
            if (this.levelUpButton) this.levelUpButton.setDisabled(!_4 || _5 == 0);
            if (this.levelDownButton) this.levelDownButton.setDisabled(!_4 || _5 == _1 - 1)
        }
        , isc.A.setFields = function isc_MultiSortPanel_setFields(_1) {
            if (isc.DataSource && isc.isA.DataSource(_1)) _1 = isc.getValues(_1.getFields());
            this.fields = _1;
            this.setSortFields();
            this.optionsGrid.refreshFields();
            this.setButtonStates()
        }
        , isc.A.setSortFields = function isc_MultiSortPanel_setSortFields() {
            var _1 = [];
            this.optionsGrid.getField("property").title = this.propertyFieldTitle;
            if (!this.fields) return;
            for (var i = 0; i < this.fields.length; i++) {
                var _3 = this.fields[i];
                if (_3.canSort != false) _1.add(_3)
            }
            this.fields = _1;
            var _4 = this.optionsGrid,
                _5 = this.fields ? this.fields.getValueMap(_4.fieldIdProperty, "title") : {none: "No fields"},
                _6 = isc.getKeys(_5).length;
            for (var _7 in _5) {
                if (isc.DataSource && (!_5[_7] || isc.isAn.emptyString(_5[_7])))
                    _5[_7] = isc.DataSource.getAutoTitle(_7)
            }
            this.optionsGrid.setValueMap("property", _5);
            if (!this.$74x || this.maxLevels > _6) this.maxLevels = _6
        }
        , isc.A.setSortDirections = function isc_MultiSortPanel_setSortDirections() {
            this.optionsGrid.getField("direction").title = this.directionFieldTitle;
            this.optionsGrid.getField("direction").valueMap = {
                "ascending": this.ascendingTitle,
                "descending": this.descendingTitle
            }
        }
        , isc.A.addLevel = function isc_MultiSortPanel_addLevel() {
            var _1 = this.optionsGrid, _2 = _1.getRecordIndex(_1.getSelectedRecord()), _3 = _1.getField("property"),
                _4 = _1.getField("direction"), _5 = _2 >= 0 ? _2 + 1 : _1.data.length,
                _6 = {property: isc.firstKey(_3.valueMap), direction: isc.firstKey(_4.valueMap)};
            _1.data.addAt(_6, _5);
            this.editRecord(_5);
            this.setButtonStates();
            this.fireChangeEvent()
        }
        , isc.A.deleteSelectedLevel = function isc_MultiSortPanel_deleteSelectedLevel() {
            var _1 = this.optionsGrid, _2 = _1.getRecordIndex(_1.getSelectedRecord());
            if (_2 >= 0) {
                _1.data.removeAt(_2);
                this.setButtonStates();
                this.fireChangeEvent()
            }
        }
        , isc.A.copySelectedLevel = function isc_MultiSortPanel_copySelectedLevel() {
            var _1 = this.optionsGrid, _2 = _1.getRecordIndex(_1.getSelectedRecord()),
                _3 = isc.shallowClone(_1.getRecord(_2));
            if (_2 >= 0) {
                _1.data.addAt(_3, _2 + 1);
                this.editRecord(_2 + 1);
                this.setButtonStates();
                this.fireChangeEvent()
            }
        }
        , isc.A.editRecord = function isc_MultiSortPanel_editRecord(_1) {
            this.optionsGrid.selectSingleRecord(_1);
            this.optionsGrid.startEditing(_1, this.propertyFieldNum)
        }
        , isc.A.moveSelectedLevelUp = function isc_MultiSortPanel_moveSelectedLevelUp() {
            var _1 = this.optionsGrid, _2 = _1.getRecordIndex(_1.getSelectedRecord());
            if (_2 > 0) {
                _1.data.slide(_2, _2 - 1);
                this.setButtonStates();
                this.fireChangeEvent();
                this.optionsGrid.selectSingleRecord(_2 - 1)
            }
        }
        , isc.A.moveSelectedLevelDown = function isc_MultiSortPanel_moveSelectedLevelDown() {
            var _1 = this.optionsGrid, _2 = _1.getRecordIndex(_1.getSelectedRecord());
            if (_2 >= 0 && _2 < _1.data.length - 1) {
                _1.data.slide(_2, _2 + 1);
                this.setButtonStates();
                this.fireChangeEvent();
                this.optionsGrid.selectSingleRecord(_2 + 1)
            }
        }
        , isc.A.fireChangeEvent = function isc_MultiSortPanel_fireChangeEvent() {
            this.sortChanged(isc.shallowClone(this.getSort()))
        }
        , isc.A.sortChanged = function isc_MultiSortPanel_sortChanged(_1) {
        }
    );
    isc.B._maxIndex = isc.C + 23;
    isc.defineClass("MultiSortDialog", "Window");
    isc.A = isc.MultiSortDialog;
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.askForSort = function isc_c_MultiSortDialog_askForSort(_1, _2, _3) {
            var _4 = isc.isAn.Array(_1) ? _1 : isc.DataSource && isc.isA.DataSource(_1) ? isc.getValues(_1.getFields()) : isc.isA.DataBoundComponent(_1) ? _1.getFields() : null;
            if (!_4) return;
            isc.MultiSortDialog.create({autoDraw: true, fields: _4, initialSort: _2, callback: _3})
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.A = isc.MultiSortDialog.getPrototype();
    isc.A.isModal = true;
    isc.A.width = 500;
    isc.A.height = 250;
    isc.A.vertical = true;
    isc.A.autoCenter = true;
    isc.A.showMinimizeButton = false;
    isc.A.mainLayoutDefaults = {_constructor: "VLayout", width: "100%", height: "100%", layoutMargin: 5};
    isc.A.multiSortPanelDefaults = {
        _constructor: "MultiSortPanel",
        width: "100%",
        height: "*",
        autoParent: "mainLayout"
    };
    isc.A.title = "Sort";
    isc.A.applyButtonTitle = "Apply";
    isc.A.cancelButtonTitle = "Cancel";
    isc.A.bottomLayoutDefaults = {
        _constructor: "HLayout",
        width: "100%",
        height: 22,
        align: "right",
        membersMargin: 5,
        autoParent: "mainLayout"
    };
    isc.A.applyButtonDefaults = {
        _constructor: "IButton",
        autoFit: true,
        height: 22,
        autoParent: "bottomLayout",
        click: "this.creator.apply()"
    };
    isc.A.cancelButtonDefaults = {
        _constructor: "IButton",
        autoFit: true,
        height: 22,
        autoParent: "bottomLayout",
        click: "this.creator.cancel()"
    };
    isc.A.bottomAutoChildren = ["bottomLayout", "applyButton", "cancelButton"];
    isc.A = isc.MultiSortDialog.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.$783 = ["fields", "initialSort", "maxLevels", "invalidListPrompt", "addLevelButtonTitle", "addLevelButtonDefaults", "addLevelButtonProperties", "deleteLevelButtonTitle", "deleteLevelButtonDefaults", "deleteLevelButtonProperties", "levelUpButtonTitle", "levelDownButtonTitle", "copyLevelButtonTitle", "copyLevelButtonDefaults", "copyLevelButtonProperties", "optionsGridDefaults", "optionsGridProperties", "firstSortLevelTitle", "propertyFieldTitle", "directionFieldTitle", "descendingTitle", "ascendingTitle", "otherSortLevelTitle"];
    isc.B.push(isc.A.initWidget = function isc_MultiSortDialog_initWidget() {
            this.Super("initWidget", arguments);
            this.addAutoChild("mainLayout");
            this.addAutoChild("multiSortPanel", this.getPassthroughProperties());
            this.addAutoChildren(this.bottomAutoChildren);
            this.addItem(this.mainLayout);
            this.optionsGrid = this.multiSortPanel.optionsGrid;
            this.setButtonStates()
        }
        , isc.A.getPassthroughProperties = function isc_MultiSortDialog_getPassthroughProperties() {
            var _1 = this.$783, _2 = {};
            for (var i = 0; i < _1.length; i++) {
                var _4 = _1[i];
                if (this[_4] != null) _2[_4] = this[_4]
            }
            return _2
        }
        , isc.A.setButtonStates = function isc_MultiSortDialog_setButtonStates() {
            this.multiSortPanel.setButtonStates();
            this.applyButton.setTitle(this.applyButtonTitle);
            this.cancelButton.setTitle(this.cancelButtonTitle)
        }
        , isc.A.getNumLevels = function isc_MultiSortDialog_getNumLevels() {
            return this.multiSortPanel.getNumLevels()
        }
        , isc.A.getSortLevel = function isc_MultiSortDialog_getSortLevel(_1) {
            return this.multiSortPanel.getSortLevel(_1)
        }
        , isc.A.getSort = function isc_MultiSortDialog_getSort() {
            return this.multiSortPanel.getSort()
        }
        , isc.A.validate = function isc_MultiSortDialog_validate() {
            return this.multiSortPanel.validate()
        }
        , isc.A.closeClick = function isc_MultiSortDialog_closeClick() {
            this.cancel();
            return false
        }
        , isc.A.cancel = function isc_MultiSortDialog_cancel() {
            if (this.callback)
                this.fireCallback(this.callback, ["sortLevels"], [null]);
            this.hide();
            this.markForDestroy()
        }
        , isc.A.apply = function isc_MultiSortDialog_apply() {
            if (this.optionsGrid.getEditRow() != null) this.optionsGrid.endEditing();
            if (!this.validate()) return;
            if (this.callback) {
                var _1 = isc.shallowClone(this.getSort());
                this.fireCallback(this.callback, ["sortLevels"], [_1])
            }
            this.hide();
            this.markForDestroy()
        }
    );
    isc.B._maxIndex = isc.C + 10;
    isc.ClassFactory.defineClass("TabSet", "Canvas");
    isc.A = isc.TabSet.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.overflow = "hidden";
    isc.A.tabProperties = {};
    isc.A.simpleTabBaseStyle = "tabButton";
    isc.A.tabBarPosition = isc.Canvas.TOP;
    isc.A.tabBarThickness = 21;
    isc.A.selectedTab = 0;
    isc.A.closeTabIcon = "[SKIN]/TabSet/close.png";
    isc.A.closeTabIconSize = 16;
    isc.A.moreTabCount = 5;
    isc.A.moreTabTitle = "More";
    isc.A.moreTabImage = "[SKINIMG]/iOS/more.png";
    isc.A.moreTabDefaults = {ariaRole: "tab"};
    isc.A.moreTabProperties = {};
    isc.A.moreTabPaneProperties = {};
    isc.A.moreTabPaneDefaults = {
        _constructor: "VLayout", width: "100%", height: "100%", setData: function (_1) {
            this.creator.moreTabPaneTable.setData(_1)
        }
    };
    isc.A.moreTabPaneNavBarDefaults = {
        _constructor: "NavigationBar",
        controls: ["titleLabel"],
        autoParent: "moreTabPane"
    };
    isc.A.moreTabPaneTableDefaults = {
        _constructor: "TableView",
        width: "100%",
        height: "100%",
        recordNavigationClick: function (_1) {
            this.creator.$8c(_1.button)
        },
        autoParent: "moreTabPane"
    };
    isc.A.tabBarControls = ["tabScroller", "tabPicker"];
    isc.A.showTabScroller = true;
    isc.A.showTabPicker = true;
    isc.A.tabBarControlLayoutConstructor = "Layout";
    isc.A.tabBarControlLayoutDefaults = {};
    isc.A.animateTabScrolling = true;
    isc.A.scrollerButtonSize = 16;
    isc.A.pickerButtonSize = 16;
    isc.A.skinImgDir = "images/TabSet/";
    isc.A.symmetricScroller = true;
    isc.A.scrollerSrc = "[SKIN]/scroll.gif";
    isc.A.scrollerHSrc = "[SKIN]hscroll.gif";
    isc.A.scrollerVSrc = "[SKIN]vscroll.gif";
    isc.A.symmetricPickerButton = true;
    isc.A.pickerButtonSrc = "[SKIN]/picker.gif";
    isc.A.pickerButtonHSrc = "[SKIN]hpicker.gif";
    isc.A.pickerButtonVSrc = "[SKIN]vpicker.gif";
    isc.A.paneContainerConstructor = "PaneContainer";
    isc.A.paneContainerClassName = "tabSetContainer";
    isc.A.paneContainerOverflow = isc.Canvas.AUTO;
    isc.A.symmetricEdges = true;
    isc.A.titleEditorDefaults = {name: "title", type: "text", showTitle: false};
    isc.B.push(isc.A.setPaneContainerOverflow = function isc_TabSet_setPaneContainerOverflow(_1) {
            this.paneContainerOverflow = _1;
            if (this.paneContainer) this.paneContainer.setOverflow(_1)
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.defineClass("SimpleTabButton", "Button");
    isc.A = isc.SimpleTabButton.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.setPane = function isc_SimpleTabButton_setPane(_1) {
            this.parentElement.parentElement.updateTab(this, _1)
        }
        , isc.A.selectTab = function isc_SimpleTabButton_selectTab() {
            this.parentElement.parentElement.selectTab(this)
        }
    );
    isc.B._maxIndex = isc.C + 2;
    isc.A = isc.TabSet.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.A.simpleTabButtonConstructor = isc.SimpleTabButton;
    isc.A.tabBarConstructor = isc.TabBar;
    isc.A.disablePaneWithTab = true;
    isc.A.$537 = {top: "topEdgeSizes", bottom: "bottomEdgeSizes", left: "leftEdgeSizes", right: "rightEdgeSizes"};
    isc.A.$538 = {
        top: "topEdgeOffsets",
        bottom: "bottomEdgeOffsets",
        left: "leftEdgeOffsets",
        right: "rightEdgeOffsets"
    };
    isc.A.namedLocatorChildren = ["tabBarControlLayout"];
    isc.B.push(isc.A.initWidget = function isc_TabSet_initWidget() {
            this.showEdges = false;
            this.Super("initWidget", arguments);
            if (this.tabs == null) this.tabs = [];
            if (this.tabBarDefaults == null) this.tabBarDefaults = {};
            this.tabProperties = this.tabProperties || this.tabInstanceDefaults || {};
            var _1 = this.tabBarPosition;
            if (this.tabBarAlign == null) {
                this.tabBarAlign = ((_1 == "left" || _1 == "right") ? "top" : "left")
            }
            if (this.useSimpleTabs) {
                this.tabProperties = isc.addProperties({}, this.tabProperties);
                this.tabBarDefaults.buttonConstructor = this.simpleTabButtonConstructor;
                this.tabProperties.baseStyle = this.simpleTabBaseStyle + _1.substring(0, 1).toUpperCase() + _1.substring(1);
                var _2 = (this.tabBarPosition == isc.Canvas.LEFT || this.tabBarPosition == isc.Canvas.RIGHT);
                if (!_2) this.tabProperties.autoFit = true;
                if (_2) this.tabProperties.autoFitDirection = isc.Canvas.VERTICAL; else this.tabProperties.autoFitDirection = isc.Canvas.HORIZONTAL;
                this.tabProperties.ariaRole = "tab"
            }
            this.makeTabBar();
            this.makePaneContainer();
            this.createPanes()
        }
        , isc.A.makeTabBar = function isc_TabSet_makeTabBar() {
            if (this.tabs == null) return;
            var _1 = (this.tabBarPosition == isc.Canvas.LEFT || this.tabBarPosition == isc.Canvas.RIGHT),
                _2 = this.tabBarAlign;
            var _3 = this.tabs.duplicate(), _4;
            for (var i = 0; i < _3.length; i++) {
                for (var j in this.tabProperties) {
                    if (_3[i][j] === _4) _3[i][j] = this.tabProperties[j]
                }
            }
            var _7 = isc.addProperties({
                selectTabOnContextClick: this.selectTabOnContextClick,
                ID: this.getID() + "_tabBar",
                width: (_1 ? this.tabBarThickness : "100%"),
                height: (_1 ? "100%" : this.tabBarThickness),
                accessKey: this.accessKey,
                tabIndex: this.tabIndex,
                tabs: _3,
                align: this.tabBarAlign,
                vertical: _1 ? true : false,
                selectedTab: this.selectedTab,
                showMoreTab: this.showMoreTab,
                moreTabCount: this.moreTabCount,
                moreTab: this.createMoreTab(),
                allowButtonReselect: this.showMoreTab ? true : false,
                buttonSelected: function (_11) {
                    this.Super("buttonSelected", arguments);
                    if (this.parentElement != null) {
                        this.parentElement.$8c(_11)
                    }
                },
                childResized: function () {
                    this.Super("childResized", arguments);
                    if (this.parentElement != null) {
                        this.parentElement.$54m()
                    }
                },
                showContextMenu: function () {
                    var _8 = isc.EH.getTarget();
                    if (this.getButtons().contains(_8)) {
                        var _9 = this.parentElement, _10 = _9.getTabObject(_8);
                        if (_9.showTabContextMenu(_9, _10) == false) return false
                    }
                    return this.Super("showContextMenu", arguments)
                },
                tabBarPosition: this.tabBarPosition,
                tabBarAlign: this.tabBarAlign,
                autoDraw: false
            }, this.tabBarDefaults, this.tabBarProperties);
            _7.creator = this;
            this.tabBar = this._tabBar = this.tabBarConstructor.create(_7);
            this.addChild(this._tabBar)
        }
        , isc.A.showTabContextMenu = function isc_TabSet_showTabContextMenu() {
        }
        , isc.A.createMoreTab = function isc_TabSet_createMoreTab() {
            if (!this.showMoreTab) return null;
            this.moreTabPane = this.createAutoChild("moreTabPane", this.moreTabPaneProperties);
            this.addAutoChild("moreTabPaneNavBar", {title: this.moreTabTitle});
            this.moreTabPaneTable = this.addAutoChild("moreTabPaneTable");
            var _1 = isc.addProperties({
                title: this.moreTabTitle,
                icon: this.moreTabImage,
                pane: this.moreTabPane,
                moreTab: true
            }, this.moreTabDefaults, this.moreTabProperties);
            var _2;
            for (var j in this.tabProperties) {
                if (_1[j] === _2) _1[j] = this.tabProperties[j]
            }
            this.moreTab = _1;
            return _1
        }
        , isc.A.rebuildMorePane = function isc_TabSet_rebuildMorePane() {
            this.moreTabPane.setData(this.getMorePaneRecords())
        }
        , isc.A.getMorePaneRecords = function isc_TabSet_getMorePaneRecords() {
            var _1 = this, _2 = [];
            for (var i = 0; i < this.tabs.length; i++) {
                var _4 = this.getTab(this.tabs[i]);
                if (_4.isVisible()) continue;
                var _5 = this.getTabObject(_4);
                var _6 = (_5.icon != null ? isc.Page.getImgURL(_5.icon) : null);
                _2[_2.length] = {icon: _6, title: _5.title, pane: _5.pane, button: _4}
            }
            return _2
        }
        , isc.A.setTabIndex = function isc_TabSet_setTabIndex(_1) {
            this.Super("setTabIndex", arguments);
            if (this._tabBar != null) this._tabBar.setTabIndex(_1)
        }
        , isc.A.setAccessKey = function isc_TabSet_setAccessKey(_1) {
            this.Super("setAccessKey", arguments);
            if (this._tabBar != null) this._tabBar.setAccessKey(_1)
        }
        , isc.A.createPanes = function isc_TabSet_createPanes() {
            for (var i = 0; i < this.tabs.length; i++) {
                var _2 = this.tabs[i], _3 = _2.pane;
                if (_3 == null) continue;
                _2.pane = this.createPane(_3, _2)
            }
        }
        , isc.A.createPane = function isc_TabSet_createPane(_1, _2) {
            if (_1 == null) return _1;
            if (!isc.isA.Canvas(_1)) _1 = this.createCanvas(_1);
            if (_1 == null) return _1;
            _1.hide();
            if (this.disablePaneWithTab && _2 && _2.disabled) {
                _1.setDisabled(_2.disabled)
            }
            this.paneContainer.addMember(_1);
            _1.$86x = this.ID;
            return _1
        }
        , isc.A.makePaneContainer = function isc_TabSet_makePaneContainer() {
            var _1 = {
                ID: this.getID() + "_paneContainer",
                _generated: false,
                className: this.paneContainerClassName,
                layoutMargin: (this.paneMargin || 0),
                overflow: this.paneContainerOverflow,
                $wn: function () {
                    var _2 = this.Super("$wn", arguments);
                    _2.addMethods({
                        $539: {top: "_top", left: "_left", bottom: "_bottom", right: "_right"},
                        getEdgePrefix: function (_7) {
                            var _3 = this.eventProxy, _4 = _3 ? _3.creator : null;
                            if (_4 && !_4.symmetricEdges) {
                                return this.$539[_4.tabBarPosition]
                            }
                        }
                    });
                    return _2
                }
            };
            if (this.showPaneContainerEdges != null) _1.showEdges = this.showPaneContainerEdges;
            if (this.getPaneContainerEdges && this.getPaneContainerEdges() != null) {
                _1.customEdges = this.getPaneContainerEdges()
            }
            if (!this.symmetricEdges) {
                var _5 = this[this.$537[this.tabBarPosition]];
                if (_5 && _5.defaultSize != null) _1.edgeSize = _5.defaultSize;
                if (_5 && _5.bottom != null) _1.edgeBottom = _5.bottom;
                if (_5 && _5.top != null) _1.edgeTop = _5.top;
                if (_5 && _5.left != null) _1.edgeLeft = _5.left;
                if (_5 && _5.right != null) _1.edgeRight = _5.right;
                var _6 = this[this.$538[this.tabBarPosition]];
                if (_6 && _6.defaultSize != null) _1.edgeOffset = _6.defaultSize;
                if (_6 && _6.bottom != null) _1.edgeOffsetBottom = _6.bottom;
                if (_6 && _6.top != null) _1.edgeOffsetTop = _6.top;
                if (_6 && _6.left != null) _1.edgeOffsetLeft = _6.left;
                if (_6 && _6.right != null) _1.edgeOffsetRight = _6.right
            }
            this.addAutoChild("paneContainer", _1)
        }
        , isc.A.getPaneContainerEdges = function isc_TabSet_getPaneContainerEdges() {
            if (this.showPartialEdges) {
                if (this.tabBarPosition == "bottom") return ["T", "L", "R"]; else if (this.tabBarPosition == "left") return ["T", "B", "R"]; else if (this.tabBarPosition == "right") return ["T", "B", "L"]; else return ["B", "L", "R"]
            }
            return null
        }
        , isc.A.draw = function isc_TabSet_draw(_1, _2, _3, _4) {
            if (this.tabs && this.tabs.length > 0) {
                var _5 = this.getSelectedTabNumber();
                if (!isc.isA.Number(_5) || _5 < 0) _5 = this.selectedTab = 0;
                this._tabBar.selectTab(_5)
            }
            this.invokeSuper(isc.TabSet, "draw", _1, _2, _3, _4);
            this.fixLayout()
        }
        , isc.A.setTabTitle = function isc_TabSet_setTabTitle(_1, _2) {
            this.getTabObject(_1).title = _2;
            this.getTab(_1).setTitle(_2);
            this.resetTabPickerMenu()
        }
        , isc.A.setTabIcon = function isc_TabSet_setTabIcon(_1, _2) {
            this.setTabProperties(_1, {icon: _2})
        }
        , isc.A.enableTab = function isc_TabSet_enableTab(_1) {
            this.setTabDisabled(_1, false)
        }
        , isc.A.disableTab = function isc_TabSet_disableTab(_1) {
            this.setTabDisabled(_1, true)
        }
        , isc.A.setTabProperties = function isc_TabSet_setTabProperties(_1, _2) {
            if (!_2) return;
            if (_2.ID != null) {
                this.logWarn("setTabProperties(): Unable to modify ID for an existing tab - ignoring " + "this property");
                delete _2.ID
            }
            if (_2.pane != null) {
                this.updateTab(_1, _2.pane);
                delete _2.pane
            }
            if (_2.disabled != null) {
                this.setTabDisabled(_1, _2.disabled);
                delete _2.disabled
            }
            var _3 = this.getTabObject(_1), _1 = this.getTab(_1);
            if (!_3) return;
            isc.addProperties(_3, _2);
            if (_1) {
                _1.setProperties(_2)
            }
            this.resetTabPickerMenu()
        }
        , isc.A.setTabDisabled = function isc_TabSet_setTabDisabled(_1, _2) {
            var _3 = this.getTabObject(_1);
            if (_3) _3.disabled = _2;
            var _1 = this.getTab(_1);
            if (_1) {
                _1.setDisabled(_2);
                var _4 = _1.pane;
                if (_4 && this.disablePaneWithTab) {
                    if (isc.isA.Canvas(_4)) _4.setDisabled(_2); else _4.disabled = _2
                }
            }
            this.resetTabPickerMenu()
        }
        , isc.A.addTab = function isc_TabSet_addTab(_1, _2) {
            return this.addTabs(_1, _2)
        }
        , isc.A.addTabs = function isc_TabSet_addTabs(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            var _3 = this.getTabObject(this.getSelectedTabNumber()), _4 = (this.getSelectedTabNumber() == -1);
            if (_2 == null || _2 > this.tabs.length) _2 = this.tabs.length;
            for (var i = 0; i < _1.length; i++) {
                _1[i].pane = this.createPane(_1[i].pane, _1[i]);
                var _6;
                for (var _7 in this.tabProperties) {
                    if (_1[i][_7] === _6) {
                        _1[i][_7] = this.tabProperties[_7]
                    }
                }
                this.tabs.addAt(_1[i], (_2 + i))
            }
            this._tabBar.addTabs(_1, _2);
            this.resetTabPickerMenu();
            this.delayCall("fixLayout");
            if (_4) {
                this.selectTab(0)
            } else {
                this.selectedTab = this.getTabNumber(_3)
            }
            this.addTabsEditModeExtras(_1);
            return _2
        }
        , isc.A.setTabPane = function isc_TabSet_setTabPane(_1, _2) {
            return this.updateTab(_1, _2)
        }
        , isc.A.removeTab = function isc_TabSet_removeTab(_1, _2) {
            return this.removeTabs(_1, _2)
        }
        , isc.A.removeTabs = function isc_TabSet_removeTabs(_1, _2) {
            if (!isc.isAn.Array(_1)) _1 = [_1];
            _1 = this.map("getTab", _1);
            var _3 = false, _4 = this.getSelectedTab(), _5 = 0;
            for (var i = 0; i < _1.length; i++) {
                var _7 = _1[i], _8 = this.getTabNumber(_7), _9 = this.tabs[_8];
                if (_9 == _4) {
                    _3 = true;
                    if (_8 > 0) _5 = _8 - 1; else if (_8 < this.tabs.length + 1) _5 = _8
                } else {
                    if (_8 < this.selectedTab) {
                        this.selectedTab -= 1
                    }
                }
                this.tabs.removeAt(_8);
                var _10 = _9.pane;
                if (_10 && _10.parentElement == this.paneContainer) {
                    this.paneContainer.removeChild(_10);
                    if (!_2 && this.destroyPanes !== false) {
                        _10.destroy()
                    }
                }
                this._tabBar.removeTabs(_7)
            }
            if (_3 && this.tabs.length > 0) {
                if (_5 >= this.tabs.length) _5 = this.tabs.length - 1;
                this.selectTab(_5)
            }
            this.resetTabPickerMenu();
            this.delayCall("fixLayout", 0);
            this.removeTabsEditModeExtras()
        }
        , isc.A.canCloseTab = function isc_TabSet_canCloseTab(_1) {
            _1 = this.getTabObject(_1);
            if (_1 && _1.canClose != null) return _1.canClose;
            return this.canCloseTabs
        }
        , isc.A.setCanCloseTab = function isc_TabSet_setCanCloseTab(_1, _2) {
            _1 = this.getTabObject(_1);
            var _3 = this.getTab(_1);
            _1.canClose = _2;
            var _4 = isc.addProperties({}, _1, {canClose: _2});
            if (_3) {
                _3.setProperties(this.getTabBar().getCloseIconProperties(_4))
            }
        }
        , isc.A.$52a = function isc_TabSet__tabIconClick(_1) {
            var _2 = this.canCloseTab(_1);
            if (_2) {
                this.closeClick(_1);
                return false
            } else return this.tabIconClick(_1)
        }
        , isc.A.closeClick = function isc_TabSet_closeClick(_1) {
            if (this.onCloseClick && (this.onCloseClick(_1) == false)) {
                return
            }
            this.removeTab(_1)
        }
        , isc.A.tabIconClick = function isc_TabSet_tabIconClick(_1) {
            var _2 = _1.icon;
            if (_2 && _2.click) return this.fireCallback(_2.click, 'tab,tabSet', [_1, this])
        }
        , isc.A.getTabObject = function isc_TabSet_getTabObject(_1) {
            _1 = this.getTabNumber(_1);
            if (_1 >= this.tabs.length) {
                var _2 = this.tabBar.getButton(_1);
                if (_2 && _2.moreTab) return this.moreTab
            }
            return this.tabs[_1]
        }
        , isc.A.getTab = function isc_TabSet_getTab(_1) {
            if (isc.isAn.Canvas(_1)) return _1;
            if (!this.tabs) return null;
            if (this.tabs.contains(_1)) _1 = this.tabs.indexOf(_1);
            _1 = this.getTabBar().getButton(_1);
            return _1
        }
        , isc.A.getTabPane = function isc_TabSet_getTabPane(_1) {
            return this.getTabObject(_1).pane
        }
        , isc.A.findTabObject = function isc_TabSet_findTabObject(_1, _2) {
            return this.tabs.find(_1, _2)
        }
        , isc.A.getTabNumber = function isc_TabSet_getTabNumber(_1) {
            if (isc.isA.Number(_1)) return _1;
            if (!this.tabs) return null;
            var _2 = this.tabs.indexOf(_1);
            if (_2 != -1) return _2;
            if (isc.isA.String(_1)) return this.tabs.findIndex("ID", _1);
            return this.getTabBar().getButtonNumber(this.getTab(_1))
        }
        , isc.A.updateTab = function isc_TabSet_updateTab(_1, _2) {
            if (isc.isAn.Object(_1) && !isc.isA.Canvas(_1) && this.tabs.indexOf(_1) == -1) {
                if (_2 != null) _1.pane = _2;
                return this.addTabs(_1)
            }
            var _3 = this.getTabNumber(_1);
            if (_3 == -1) {
                this.logWarn("no such tab: " + this.echo(_1));
                return
            }
            var _4 = this.getTabObject(_3), _5 = _4 ? _4.pane : null;
            if (_4 && _4.pane == _2) return;
            if (_5 != null) {
                _5.hide();
                _5.deparent()
            }
            var _6 = this.getTab(_1);
            if (_2 == null) {
                if (_6 != null) _6.pane = null;
                return _4.pane = null
            }
            _2 = _4.pane = this.createPane(_2, _4);
            if (_6 != null) _6.pane = _2;
            if (this.getSelectedTabNumber() == _3) {
                if (!this.paneContainer.hasMember(_2)) this.paneContainer.addMember(_2);
                _2.setVisibility(isc.Canvas.INHERIT)
            }
        }
        , isc.A.fixLayout = function isc_TabSet_fixLayout() {
            var _1 = this._tabBar, _2 = this.$l0 || this.paneContainer;
            if (_1 == null || _2 == null) return;
            if (_2.getZIndex(true) >= _1.getZIndex(true)) _2.moveBelow(_1);
            var _3 = this.$du(this.tabBarOverlap, _1.borderThickness, _1.baseLineThickness);
            var _4;
            switch (this.tabBarPosition) {
                case isc.Canvas.TOP:
                    _4 = false;
                    _2.setRect(0, _1.getHeight() - _3, this.getWidth(), this.getHeight() - _1.getHeight() + _3);
                    break;
                case isc.Canvas.BOTTOM:
                    _4 = false;
                    _1.setTop(this.getHeight() - _1.getHeight());
                    _2.setRect(0, 0, this.getWidth(), this.getHeight() - _1.getHeight() + _3);
                    break;
                case isc.Canvas.LEFT:
                    _4 = true;
                    _2.setRect(_1.getWidth() - _3, 0, this.getWidth() - _1.getWidth() + _3, this.getHeight());
                    break;
                case isc.Canvas.RIGHT:
                    _4 = true;
                    _1.setLeft(this.getWidth() - _1.getWidth());
                    _2.setRect(0, 0, this.getWidth() - _1.getWidth() + _3, this.getHeight());
                    break
            }
            var _5 = this.showControls();
            if (_5) {
                if (_4) _1.setHeight(this.getViewportHeight() - this.tabBarControlLayout.getHeight()); else _1.setWidth(this.getViewportWidth() - this.tabBarControlLayout.getWidth());
                this.tabBarControlLayout.bringToFront()
            } else {
                _1.resizeTo(_4 ? null : "100%", _4 ? "100%" : null)
            }
            var _6 = this.$8f();
            if (_4) {
                if (_1.getScrollTop() > 0 && _6 <= _1.getViewportHeight()) _1.scrollTo(null, 0, "descrollTabs")
            } else {
                if (_1.getScrollLeft() > 0 && _6 <= _1.getViewportWidth()) _1.scrollTo(0, null, "descrollTabs")
            }
        }
        , isc.A.shouldShowControl = function isc_TabSet_shouldShowControl(_1) {
            if ((_1 == "tabScroller") || (_1 == "tabPicker")) {
                if (this.showMoreTab) return false;
                if (!this.showTabScroller && _1 == "tabScroller") return false;
                if (!this.showTabPicker && _1 == "tabPicker") return false;
                var _2 = this.$8f();
                if (_2 == 0) return;
                var _3 = 0;
                for (var i = 0; i < this.tabBarControls.length; i++) {
                    var _5 = this.tabBarControls[i];
                    if (_5 == "tabScroller" || _5 == "tabPicker") continue;
                    if (this.shouldShowControl(_5)) {
                        if (!isc.isA.Canvas(_5)) _5 = this.getControl(_5);
                        _3 += _6 ? _5.getVisibleHeight() : _5.getVisibleWidth()
                    }
                }
                var _6 = (this._tabBar.orientation == isc.Layout.VERTICAL),
                    _7 = (_2 > (_6 ? (this.getViewportHeight() - _3) : (this.getViewportWidth() - _3)));
                return _7
            }
            var _1 = this.getControl(_1);
            if (isc.isA.Canvas(_1)) {
                if (_1.showIf) return _1.fireCallback(_1.showIf, [_1]); else return true
            }
        }
        , isc.A.$8f = function isc_TabSet__getTabSizes() {
            if (!this._tabBar) return 0;
            var _1 = this._tabBar.getMemberSizes(), _2 = this._tabBar.vertical;
            if (_1 == null || _1.length == 0) return 0;
            _1 = _1.sum();
            var _3 = (_2 ? (this._tabBar.$td || 0) + (this._tabBar.$te || 0) : (this._tabBar.$tb || 0) + (this._tabBar.$tc || 0));
            return _1 + _3
        }
        , isc.A.getControl = function isc_TabSet_getControl(_1) {
            if (isc.isA.Canvas(_1)) return _1;
            var _2 = (this._tabBar.orientation == isc.Layout.VERTICAL);
            if (_1 == "tabScroller") {
                if (!this.scroller) {
                    var _3 = this.scrollerButtonSize;
                    var _4;
                    if (this.symmetricScroller) {
                        _4 = _2 ? this.scrollerVSrc : this.scrollerHSrc
                    } else {
                        _4 = this.scrollerSrc
                    }
                    var _5 = this.symmetricScroller ? "back" : this.tabBarPosition + "_back",
                        _6 = this.symmetricScroller ? "forward" : this.tabBarPosition + "_forward";
                    this.scroller = isc.StretchImgButton.create({
                        noDoubleClicks: true,
                        tabSet: this,
                        vertical: _2,
                        width: _2 ? (this.tabBarThickness - this._tabBar.baseLineThickness) : (2 * _3),
                        height: _2 ? (2 * _3) : (this.tabBarThickness - this._tabBar.baseLineThickness),
                        items: [{name: _5, width: _2 ? null : _3, height: _2 ? _3 : null}, {
                            name: _6,
                            width: _2 ? null : _3,
                            height: _2 ? _3 : null
                        }],
                        skinImgDir: this.skinImgDir,
                        src: _4,
                        showRollOver: false,
                        showDown: false,
                        backPartName: _5,
                        forwardPartName: _6,
                        mouseMove: function () {
                            if (!this.tabSet.showScrollerRollOver) return;
                            var _7 = this.inWhichPart();
                            var _8 = _7 == this.backPartName ? this.forwardPartName : this.backPartName;
                            this.setState(isc.StatefulCanvas.STATE_UP, _8);
                            this.setState(isc.StatefulCanvas.STATE_OVER, _7)
                        },
                        mouseOut: function () {
                            if (!this.tabSet.showScrollerRollOver) return;
                            this.setState(isc.StatefulCanvas.STATE_UP, this.forwardPartName);
                            this.setState(isc.StatefulCanvas.STATE_UP, this.backPartName)
                        },
                        mouseDown: function () {
                            this.clickPart = this.inWhichPart();
                            this.setState(isc.StatefulCanvas.STATE_DOWN, this.clickPart)
                        },
                        mouseUp: function () {
                            this.setState(isc.StatefulCanvas.STATE_UP, this.clickPart)
                        },
                        mouseStillDown: function () {
                            this.click()
                        },
                        click: function () {
                            var _9 = this.clickPart == this.backPartName;
                            if (_9) this.tabSet.scrollBack(); else this.tabSet.scrollForward();
                            return false
                        }
                    }, this.scrollerProperties)
                }
                return this.scroller
            } else if (_1 == "tabPicker") {
                var _10 = this.pickerButtonSize;
                if (!this.tabPicker) {
                    var _11;
                    if (this.symmetricPickerButton) {
                        _11 = _2 ? this.pickerButtonVSrc : this.pickerButtonHSrc
                    } else {
                        _11 = this.pickerButtonSrc
                    }
                    this.tabPicker = isc.ImgButton.create({
                        customState: this.symmetricPickerButton ? null : this.tabBarPosition,
                        tabSet: this,
                        showRollOver: false,
                        skinImgDir: this.skinImgDir,
                        src: _11,
                        height: (_2 ? _10 : (this.tabBarThickness - this._tabBar.baseLineThickness)),
                        width: (_2 ? (this.tabBarThickness - this._tabBar.baseLineThickness) : _10),
                        click: function () {
                            this.tabSet.showTabPickerMenu()
                        }
                    }, this.tabPickerProperties)
                }
                return this.tabPicker
            }
            if (isc.isA.String(_1) && isc.isA.Canvas(window[_1])) return window[_1];
            this.logWarn("Unable to resolve specified tabBarControl:" + isc.Log.echo(_1) + " to a valid control. Not displaying.");
            return null
        }
        , isc.A.showControls = function isc_TabSet_showControls() {
            var _1 = this.tabBarControls, _2 = 0, _3 = this.tabBarPosition,
                _4 = _3 == isc.Canvas.RIGHT || _3 == isc.Canvas.LEFT, _5 = 0;
            for (var i = 0; i < _1.length; i++) {
                var _7 = _1[i];
                if (!this.shouldShowControl(_7)) continue;
                _7 = this.getControl(_7);
                if (!_7) continue;
                var _8 = this.tabBarControlLayout;
                if (!_8) {
                    this.tabBarControlLayout = _8 = this.createAutoChild("tabBarControlLayout", {
                        styleName: this.tabBarControlLayoutDefaults.styleName || this.tabBar.styleName,
                        childResized: function () {
                            this.Super("childResized", arguments);
                            this.creator.$510()
                        },
                        vertical: _4,
                        locatorParent: this
                    })
                }
                if (_8.getMemberNumber(_7) != _5) {
                    _8.addMember(_7, _5)
                }
                _5++;
                _2 += _4 ? _7.getVisibleHeight() : _7.getVisibleWidth()
            }
            if (_8 && _8.members) {
                var _9 = [];
                for (var i = _5; i < _8.members.length; i++) {
                    _9.add(i)
                }
                _8.removeMembers(_9)
            }
            if (_5 == 0) {
                this.hideControls();
                return false
            }
            this.placeControlLayout(_2);
            if (!this.$8g) {
                var _10 = this._tabBar;
                this.$8g = this._tabBar.createAutoChild("baseLine", {
                    vertical: (_3 == isc.Canvas.LEFT || _3 == isc.Canvas.RIGHT),
                    _generated: true,
                    skinImgDir: _10.skinImgDir,
                    src: _10.baseLineSrc,
                    capSize: _10.baseLineCapSize,
                    imageType: isc.Img.STRETCH,
                    overflow: "hidden",
                    autoDraw: false
                });
                this.addChild(this.$8g)
            }
            var _10 = this._tabBar, _11 = (this.tabBarThickness - _10.baseLineThickness);
            if (_3 == isc.Canvas.LEFT) {
                this.$8g.setRect(_11, 0, _10.baseLineThickness, this.getHeight())
            } else if (_3 == isc.Canvas.RIGHT) {
                this.$8g.setRect(this.getWidth() - this.tabBarThickness, 0, _10.baseLineThickness, this.getHeight())
            } else if (_3 == isc.Canvas.TOP) {
                this.$8g.setRect(0, _11, this.getWidth(), _10.baseLineThickness)
            } else if (_3 == isc.Canvas.BOTTOM) {
                this.$8g.setRect(0, this.getHeight() - this.tabBarThickness, this.getWidth(), _10.baseLineThickness)
            }
            if (!_8.isVisible()) _8.show();
            this.$8g.moveBelow(_10);
            if (!this.$8g.isVisible()) this.$8g.show();
            return true
        }
        , isc.A.placeControlLayout = function isc_TabSet_placeControlLayout(_1) {
            var _2, _3, _4, _5, _6 = this._tabBar, _7 = _6.getBreadth() - _6.baseLineThickness,
                _8 = this.tabBarPosition;
            if (_8 == isc.Canvas.LEFT) {
                _2 = 0;
                _3 = this.getHeight() - _1;
                _4 = _7;
                _5 = _1
            } else if (_8 == isc.Canvas.RIGHT) {
                _2 = this.getWidth() - _7;
                _3 = this.getHeight() - _1;
                _4 = _7;
                _5 = _1
            } else if (_8 == isc.Canvas.BOTTOM) {
                _4 = _1;
                _2 = this.getWidth() - _1;
                _3 = this.getHeight() - _7;
                _5 = _7
            } else {
                _4 = _1;
                _2 = this.getWidth() - _1;
                _3 = 0;
                _5 = _7
            }
            this.tabBarControlLayout.setRect(_2, _3, _4, _5);
            if (!this.children.contains(this.tabBarControlLayout)) this.addChild(this.tabBarControlLayout)
        }
        , isc.A.$510 = function isc_TabSet__controlLayoutChildResized() {
            var _1 = this.tabBarControlLayout;
            if (!_1 || !_1.isDrawn() || !_1.isVisible()) return;
            var _2 = 0;
            for (var i = 0; i < _1.members.length; i++) {
                if (_1.vertical) _2 += _1.members[i].getVisibleHeight(); else _2 += _1.members[i].getVisibleWidth()
            }
            this.placeControlLayout(_2);
            var _4 = this.tabBar;
            if (_4) {
                var _5 = (this.tabBarPosition == isc.Canvas.LEFT || this.tabBarPosition == isc.Canvas.RIGHT);
                if (_5) {
                    _4.setHeight(this.getViewportHeight() - this.tabBarControlLayout.getVisibleHeight())
                } else {
                    _4.setWidth(this.getViewportWidth() - this.tabBarControlLayout.getVisibleWidth())
                }
            }
        }
        , isc.A.hideControls = function isc_TabSet_hideControls() {
            if (this.tabBarControlLayout && this.tabBarControlLayout.isVisible()) this.tabBarControlLayout.hide();
            if (this.$8g && this.$8g.isVisible()) this.$8g.hide()
        }
        , isc.A.scrollForward = function isc_TabSet_scrollForward() {
            this._tabBar.scrollForward(this.animateTabScrolling)
        }
        , isc.A.scrollBack = function isc_TabSet_scrollBack() {
            this._tabBar.scrollBack(this.animateTabScrolling)
        }
        , isc.A.showTabPickerMenu = function isc_TabSet_showTabPickerMenu() {
            if (!this.$8d) {
                var _1 = this.tabs, _2 = [];
                for (var i = 0; i < _1.length; i++) {
                    _2[i] = {
                        index: i,
                        enabled: !this.tabs[i].disabled,
                        checkIf: "this.tabSet.getSelectedTabNumber() == " + i,
                        title: _1[i].pickerTitle || _1[i].title,
                        icon: (this.canCloseTab(_1[i]) ? null : _1[i].icon),
                        click: "menu.tabSet.selectTab(item.index)"
                    }
                }
                this.$8d = this.getMenuConstructor().create({tabSet: this, data: _2})
            }
            this.$8d.$8h();
            this.$8d.placeNear(this.tabPicker.getPageLeft(), this.tabPicker.getPageBottom());
            this.$8d.show()
        }
        , isc.A.resetTabPickerMenu = function isc_TabSet_resetTabPickerMenu() {
            if (this.$8d) {
                this.$8d.destroy();
                delete this.$8d
            }
        }
        , isc.A.layoutChildren = function isc_TabSet_layoutChildren(_1, _2, _3, _4) {
            this.invokeSuper(isc.TabSet, "layoutChildren", _1, _2, _3, _4);
            this.fixLayout()
        }
        , isc.A.$54m = function isc_TabSet__tabResized() {
            this.fixLayout()
        }
        , isc.A.$8i = function isc_TabSet__showTab(_1) {
            if (isc.isA.Canvas(_1)) _1 = this.getTabObject(_1);
            if (_1 == this.moreTab) {
                this.rebuildMorePane()
            }
            this.paneContainer.scrollTo(0, 0, "showTab");
            if (_1 && _1.pane) {
                if (!this.paneContainer.hasMember(_1.pane)) this.paneContainer.addMember(_1.pane);
                _1.pane.show()
            }
            this.paneContainer.adjustOverflow()
        }
        , isc.A.$8c = function isc_TabSet__tabSelected(_1) {
            var _2;
            var _3 = this.getSelectedTab(), _4 = this.getSelectedTabNumber(), _5 = this._tabBar.getButtonNumber(_1),
                _6 = this.getTabObject(_5), _7 = (_3 != null) && (_6 != _3);
            var _8 = this.showMoreTab && this.tabBar.isShowingMoreTab() && _6 == this.moreTab;
            if (!_8) {
                if (_6 == this.$80n) return;
                this.$80n = _6
            }
            if (_7 && !this.$767) {
                if (_3.tabDeselected != null) {
                    if (this.fireCallback(_3.tabDeselected, "tabSet,tabNum, tabPane, ID, tab, newTab", [this, this.selectedTab, _3.pane, _3.ID, _3, _6]) == false) {
                        _2 = true
                    }
                }
                if (!_2 && this.tabDeselected != null) {
                    _2 = (this.tabDeselected(this.selectedTab, _3.pane, _3.ID, _3, _6) == false)
                }
                if (!_2 && _3.pane) {
                    _3.pane.hide()
                }
            }
            if (_2) {
                this.$767 = true;
                var _1 = this.getSelectedTab();
                this.selectTab(_1);
                var _9 = this.getTab(this.getTabNumber(_1));
                if (isc.EH.clickMaskUp() && isc.EH.targetIsMasked(_9)) {
                    var _10 = isc.EH.clickMaskRegistry.last();
                    isc.EH.setMaskedFocusCanvas(_9, _10)
                }
                delete this.$767;
                return
            }
            this.selectedTab = _5;
            if (!this.$767) {
                var _11;
                if (_6.tabSelected != null) {
                    this.fireCallback(_6.tabSelected, "tabSet,tabNum,tabPane,ID,tab", [this, _5, _6.pane, _6.ID, _6]);
                    if (this.getSelectedTabNumber() != _5) {
                        return
                    }
                }
                if (this.tabSelected) {
                    this.tabSelected(_5, _6.pane, _6.ID, _6);
                    if (this.getSelectedTabNumber() != _5) {
                        return
                    }
                }
            }
            this.$8i(_1);
            var _12 = this._tabBar;
            var _13 = this;
            _12.scrollTabIntoView(_5, null, this.animateTabScrolling, function () {
                if (isc.isA.Function(_13.tabScrolledIntoView)) _13.tabScrolledIntoView()
            })
        }
        , isc.A.getSelectedTab = function isc_TabSet_getSelectedTab() {
            if (this.selectedTab >= this.tabs.length) return this.moreTab;
            return this.tabs[this.selectedTab]
        }
        , isc.A.getSelectedTabNumber = function isc_TabSet_getSelectedTabNumber() {
            if (!isc.isA.Number(this.selectedTab)) this.selectedTab = this.getTabNumber(this.selectedTab);
            if (!this.tabs || !this.tabs[this.selectedTab]) return -1;
            return this.selectedTab
        }
        , isc.A.selectTab = function isc_TabSet_selectTab(_1) {
            var _2 = this.getTabNumber(_1);
            if (_2 != -1) {
                if (this._tabBar) {
                    this._tabBar.selectTab(_2)
                }
                if (this._tabBar == null || !this._tabBar.$6c) {
                    this.selectedTab = _2
                }
            }
        }
        , isc.A.tabForPane = function isc_TabSet_tabForPane(_1) {
            if (this.tabs) {
                for (var i = 0; i < this.tabs.length; i++) {
                    if (this.tabs[i].pane == _1) {
                        return this.tabs[i]
                    }
                }
            }
        }
        , isc.A.getTabBar = function isc_TabSet_getTabBar() {
            return this._tabBar
        }
        , isc.A.$798 = function isc_TabSet__editTabTitle(_1) {
            _1 = this.getTab(_1);
            var _2;
            if (this.canEditTabTitles) {
                if (_1.canEditTitle !== false) {
                    _2 = true
                }
            } else {
                if (_1.canEditTitle === true) {
                    _2 = true
                }
            }
            if (_2) this.editTabTitle(_1);
            return _2
        }
        , isc.A.editTabTitle = function isc_TabSet_editTabTitle(_1) {
            _1 = this.getTab(_1);
            if (_1 == null || !this.tabBar) return;
            if (!isc.isA.DynamicForm(this.titleEditorForm)) {
                var _2 = isc.addProperties({}, this.titleEditorDefaults, this.titleEditorProperties, {
                    handleKeyPress: function (_8, _9) {
                        var _3 = this.Super("handleKeyPress", arguments);
                        var _4 = _8.keyName;
                        if (_4 == "Escape") {
                            this.form.targetTabSet.cancelTabTitleEditing()
                        } else if (_4 == "Enter") {
                            this.form.targetTabSet.saveTabTitle()
                        }
                        return _3
                    }
                });
                _2.name = "title";
                this.titleEditorForm = isc.DynamicForm.create({
                    autoDraw: false,
                    margin: 0,
                    padding: 0,
                    cellPadding: 0,
                    fields: [_2]
                });
                this.titleEditor = this.titleEditorForm.getItem("title")
            }
            var _5 = this.titleEditorForm;
            _5.setProperties({targetTabSet: this, targetTab: _1});
            var _6 = _5.getItem("title");
            var _7 = _1.title;
            _6.setValue(_7);
            this.tabBar.scrollTabIntoView(_1, null, this.animateTabScrolling, {
                target: this,
                methodName: "showTitleEditor"
            })
        }
        , isc.A.cancelTabTitleEditing = function isc_TabSet_cancelTabTitleEditing() {
            if (this.titleEditorForm != null) {
                this.clearTitleEditorForm()
            }
        }
        , isc.A.saveTabTitle = function isc_TabSet_saveTabTitle() {
            if (this.titleEditorForm != null && this.titleEditorForm.isVisible() && this.titleEditorForm.isDrawn()) {
                var _1 = false, _2 = this.titleEditorForm, _3 = _2.targetTab, _4 = _2.getValue("title");
                if (_4 != _3.title && (this.titleChanged != null)) {
                    if (this.fireCallback(this.titleChanged, "newTitle, oldTitle, tab", [_4, _3.title, _3]) == false) {
                        _1 = true
                    }
                }
                if (!_1) this.setTabTitle(_2.targetTab, _4)
            }
            this.clearTitleEditorForm()
        }
        , isc.A.clearTitleEditorForm = function isc_TabSet_clearTitleEditorForm() {
            if (this.titleEditorForm == null) return;
            this.titleEditorForm.clear();
            if (this.titleEditorForm.$803 != null) {
                isc.Page.clearEvent(this.$803);
                delete this.$803
            }
            this.titleEditorForm.targetTab = null
        }
        , isc.A.showTitleEditor = function isc_TabSet_showTitleEditor() {
            var _1 = this.titleEditorForm, _2 = _1 ? _1.targetTab : null;
            if (_2 == null || !this.getTabObject(_2)) {
                return
            }
            var _3 = this.tabBar.getLeft() + this.tabBar.getLeftMargin() - this.tabBar.getScrollLeft() + this.tabBar.getLeftBorderSize() + _2.getLeft() + _2.capSize,
                _4 = _2.getVisibleWidth() - _2.capSize * 2;
            if (this.titleEditorLeftOffset) {
                _3 += this.titleEditorLeftOffset;
                _4 -= this.titleEditorLeftOffset
            }
            if (this.titleEditorRightOffset) {
                _4 -= this.titleEditorRightOffset
            }
            var _5 = _1.getItem("title");
            _5.setWidth(_4);
            var _6 = this.getTop() + this.tabBar.getTop() + this.tabBar.getTopMargin() - this.tabBar.getScrollTop() + this.tabBar.getTopBorderSize() + _2.getTop();
            if (this.titleEditorTopOffset) {
                _6 += this.titleEditorTopOffset
            }
            _1.setTop(_6);
            _1.setLeft(_3);
            var _5 = _1.getItem("title");
            if (_1.masterElement != this) {
                _1.$ns = true;
                _1.$jo = false;
                _1.$jq = false;
                this.addPeer(_1)
            } else {
                _1.draw()
            }
            _5.focusInItem();
            _5.delayCall("selectValue", [], 100);
            if (this.$803 == null) {
                var _7 = this;
                var _8 = function () {
                    if (!_7.destroyed) {
                        _7.$804()
                    }
                };
                this.$803 = isc.Page.setEvent("mouseDown", _8)
            }
        }
        , isc.A.$804 = function isc_TabSet__clickOutsideDuringTitleEdit() {
            if (isc.EH.getTarget() == this.titleEditorForm) return;
            this.saveTabTitle()
        }
        , isc.A.clear = function isc_TabSet_clear(_1, _2, _3, _4) {
            if (this.titleEditorForm != null && this.titleEditorForm.isDrawn()) {
                this.cancelTitleEditing()
            }
            this.invokeSuper("TabSet", "clear", _1, _2, _3, _4)
        }
        , isc.A.setVisibility = function isc_TabSet_setVisibility(_1, _2, _3, _4, _5) {
            this.invokeSuper("TabSet", "setVisibility", _1, _2, _3, _4, _5);
            if (!this.isVisible() && this.titleEditorForm != null && this.titleEditorForm.isDrawn()) {
                this.cancelTitleEditing()
            }
        }
        , isc.A.parentVisibilityChanged = function isc_TabSet_parentVisibilityChanged(_1, _2, _3, _4, _5) {
            this.invokeSuper("TabSet", "parentVisibilityChanged", _1, _2, _3, _4, _5);
            if (!this.isVisible() && this.titleEditorForm != null && this.titleEditorForm.isDrawn()) {
                this.cancelTitleEditing()
            }
        }
    );
    isc.B._maxIndex = isc.C + 66;
    isc.TabSet.registerStringMethods({
        tabSelected: "tabNum,tabPane,ID,tab",
        tabDeselected: "tabNum,tabPane,ID,tab,newTab",
        getPaneContainerEdges: "",
        onCloseClick: "tab",
        titleChanged: "newTitle,oldTitle,tab",
        showTabContextMenu: "tabSet,tab"
    });
    isc.defineClass("PaneContainer", "VLayout");
    isc.A = isc.PaneContainer.getPrototype();
    isc.B = isc._allFuncs;
    isc.C = isc.B._maxIndex;
    isc.D = isc._funcClasses;
    isc.D[isc.C] = isc.A.Class;
    isc.B.push(isc.A.handleKeyPress = function isc_PaneContainer_handleKeyPress(_1, _2) {
            if (_1.keyName == "Tab" && _1.ctrlKey) {
                var _3 = this.parentElement, _4 = _3.tabs.length - 1, _5 = _3.getSelectedTabNumber();
                if (_1.shiftKey) {
                    if (_5 > 0) _5 -= 1; else _5 = _4
                } else {
                    if (_5 < _4) _5 += 1; else _5 = 0
                }
                _3.selectTab(_5);
                _3.getTabBar().getButton(_5).focus();
                return false
            }
            if (this.convertToMethod("keyPress")) return this.keyPress(_1, _2)
        }
    );
    isc.B._maxIndex = isc.C + 1;
    isc.TabSet.registerDupProperties("tabs", ["pane"]);
    isc._moduleEnd = isc._Containers_end = (isc.timestamp ? isc.timestamp() : new Date().getTime());
    if (isc.Log && isc.Log.logIsInfoEnabled('loadTime')) isc.Log.logInfo('Containers module init time: ' + (isc._moduleEnd - isc._moduleStart) + 'ms', 'loadTime');
    delete isc.definingFramework;
} else {
    if (window.isc && isc.Log && isc.Log.logWarn) isc.Log.logWarn("Duplicate load of module 'Containers'.");
}
/*
 * Isomorphic SmartClient
 * Version v8.2p_2012-06-03 (2012-06-03)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */

