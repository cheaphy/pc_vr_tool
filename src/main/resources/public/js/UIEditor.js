(function(ace){
    var obj = function(){};
    if(ace){
        obj = function(config){
            var options = config || {};
            var name = options.name,
                id = options.id,
                type = options.type;
            var $editor = document.getElementById(id);
            if($editor){
                delete options.id;
                delete options.name;
                delete options.type;
                var editor = ace.edit(id);
                editor.setOption("highlightActiveLine", true);
                editor.setOption("enableBasicAutocompletion", true);
                editor.setOption("enableSnippets", true);
                editor.setOption("enableLiveAutocompletion", true);
                editor.setOptions(options);
                if(type){
                    editor.getSession().setMode("ace/mode/" + type);
                }
                if(name){
                    $editor.querySelector("textarea").setAttribute("name", name);
                }
                editor.commands.addCommands([
                    {
                        name: "toggleUserWrap",
                        bindKey: {win: "Ctrl-q", mac: "Command-q", sender: "editor"},
                        exec: function(editor){
                            var isOn = editor.isWrapOn;
                            if(isOn){
                                editor.getSession().setUseWrapMode(false);
                            }
                            else{
                                editor.getSession().setUseWrapMode(true);
                                editor.getSession().setWrapLimitRange();
                            }
                            editor.isWrapOn = !isOn;
                        },
                        readOnly: false
                    },{
                        name: "bueatifyCode",
                        bindKey: {win: "Ctrl-b", mac: "Command-b"},
                        exec: function(editor){
                            if(type == "js"){
                                var Beautify = ace.require("ace/ext/beautify");
                                Beautify.beautify(editor.getSession());
                            }
                            else if(type == "xml" || type == "xsd"){
                                var value = editor.getValue();
                                var newValue = vkbeautify.xml(value, 4);
                                editor.getSession().doc.setValue(newValue);
                            }
                        }
                    }]);
                this.options = options;
                this.type = type;
                this.id = id;
                this.name = name;
                this.editor = editor;
                this.$editor = $editor;
            }
        }
    }
    window.UIEditor = obj
})(window.ace);