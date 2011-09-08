/*
 [The "BSD license"]
 Copyright (c) 2011 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.antlr.v4.runtime.tree;

import org.antlr.v4.runtime.ParserRuleContext;

public class ParseTreeVisitor {
    public void visit(ParseTreeListener listener, ParseTree t) {
		if ( t instanceof ParseTree.TokenNode) {
			visitToken(listener, (ParseTree.TokenNode) t);
			return;
		}
		ParseTree.RuleNode r = (ParseTree.RuleNode)t;
        enterRule(listener, r);
        int n = r.getChildCount();
        for (int i = 0; i<n; i++) {
            visit(listener, r.getChild(i));
        }
		exitRule(listener, r);
    }

    protected void visitToken(ParseTreeListener listener, ParseTree.TokenNode t) {
        listener.visitToken(t.getToken());
    }

	/** The discovery of a rule node, involves sending two events:
	 *  the generic discoverRule and a RuleContext-specific event.
	 *  First we trigger the generic and then the rule specific.
	 *  We to them in reverse order upon finishing the node.
	 */
    protected void enterRule(ParseTreeListener listener, ParseTree.RuleNode r) {
		ParserRuleContext ctx = (ParserRuleContext)r.getRuleContext();
		listener.discoverRule((ParserRuleContext)r.getRuleContext());
		ctx.enterRule(listener);
    }

    protected void exitRule(ParseTreeListener listener, ParseTree.RuleNode r) {
		ParserRuleContext ctx = (ParserRuleContext)r.getRuleContext();
		ctx.exitRule(listener);
		listener.finishRule(ctx);
    }

	/*
	public static void main(String[] args) {
		TListener listener = new TListener() {
			public void discover_s(s_ctx ctx) {
			}
			public void finish_s(s_ctx ctx) {
			}
			public void discover_ifstat(ParserRuleContext ctx) {
			}
			public void finish_ifstat(ParserRuleContext ctx) {
			}
			public void visitToken(Token token) {
			}
			public void discoverRule(ParserRuleContext ctx) {
			}
			public void finishRule(ParserRuleContext ctx) {
			}
		};
		ParserRuleContext ctx = new ParserRuleContext();
		ParseTreeVisitor visitor = new ParseTreeVisitor();
		visitor.visit(listener, ctx);
	}
	*/
}

/*
interface TListener extends ParseTreeListener {
	void discover_s(s_ctx ctx);
	void finish_s(s_ctx ctx);
	void discover_ifstat(ParserRuleContext ctx); // no labels
	void finish_ifstat(ParserRuleContext ctx); // no labels
}

class s_ctx extends ParserRuleContext {
    public ParserRuleContext i;
    public s_ctx(RuleContext parent, int state) {
            super(parent, state);
    }
	public void discover(TListener listener) { listener.discover_s(this); }
	public void finish(TListener listener) { listener.discover_s(this); }
}
*/